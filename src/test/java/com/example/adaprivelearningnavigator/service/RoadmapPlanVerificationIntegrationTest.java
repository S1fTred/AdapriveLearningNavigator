package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.PrereqStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanBuildRequest;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanStepExplanationResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanStepResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanWeekResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:file:./db/aln_db;AUTO_SERVER=TRUE;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=aln",
        "spring.datasource.password=aln",
        "spring.sql.init.mode=never",
        "app.browser.auto-open=false",
        "app.roadmap-sh.bootstrap.enabled=false",
        "app.roadmap-sh.catalog-sync.enabled=false",
        "app.roadmap-sh.roadmap-sync.enabled=false"
})
@Transactional
class RoadmapPlanVerificationIntegrationTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private RoleGoalRepository roleGoalRepository;

    @Autowired
    private RoleTopicRepository roleTopicRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldBuildValidPlansForSeveralImportedRoadmaps() {
        User user = createVerificationUser();
        List<String> roleCodes = List.of(
                "java-backend",
                "frontend",
                "devops",
                "ai-agents",
                "game-developer"
        );

        for (String roleCode : roleCodes) {
            RoleGoal role = getActiveRole(roleCode);
            List<RoleTopic> roleTopics = roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(role.getId());

            assertFalse(roleTopics.isEmpty(), "У roadmap '%s' нет тем в KB".formatted(roleCode));

            Set<Long> knownTopicIds = firstRequiredTopicIds(roleTopics, 1);
            int hoursPerWeek = safeWeeklyBudget(roleTopics);

            PlanFullResponse response = planService.buildPlanFromRoadmap(
                    user.getId(),
                    new PlanBuildRequest(role.getId(), hoursPerWeek, knownTopicIds, null)
            );

            assertValidPlan(role, roleTopics, knownTopicIds, hoursPerWeek, response);
        }
    }

    @Test
    void shouldAdaptWeekCountWhenBudgetChangesForJavaBackend() {
        User user = createVerificationUser();
        RoleGoal role = getActiveRole("java-backend");
        List<RoleTopic> roleTopics = roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(role.getId());

        int minSupportedBudget = safeWeeklyBudget(roleTopics);
        int largerBudget = minSupportedBudget + 4;

        PlanFullResponse compactPlan = planService.buildPlanFromRoadmap(
                user.getId(),
                new PlanBuildRequest(role.getId(), minSupportedBudget, Set.of(), null)
        );
        PlanFullResponse relaxedPlan = planService.buildPlanFromRoadmap(
                user.getId(),
                new PlanBuildRequest(role.getId(), largerBudget, Set.of(), null)
        );

        List<Long> compactTopicOrder = flattenSteps(compactPlan).stream()
                .map(PlanStepResponse::topicId)
                .toList();
        List<Long> relaxedTopicOrder = flattenSteps(relaxedPlan).stream()
                .map(PlanStepResponse::topicId)
                .toList();

        assertEquals(relaxedTopicOrder, compactTopicOrder,
                "Порядок тем не должен меняться при изменении недельного лимита");
        assertTrue(compactPlan.weeks().size() >= relaxedPlan.weeks().size(),
                "При меньшем лимите часов количество недель не должно уменьшаться");

        assertAllWeeksRespectBudget(compactPlan.weeks(), minSupportedBudget);
        assertAllWeeksRespectBudget(relaxedPlan.weeks(), largerBudget);
    }

    private void assertValidPlan(RoleGoal role,
                                 List<RoleTopic> roleTopics,
                                 Set<Long> knownTopicIds,
                                 int hoursPerWeek,
                                 PlanFullResponse response) {
        assertEquals(role.getId(), response.roleId());
        assertNotNull(response.params(), "У плана должен быть snapshot параметров");
        assertEquals(hoursPerWeek, response.params().hoursPerWeek());
        assertFalse(response.weeks().isEmpty(), "План должен содержать хотя бы одну неделю");

        List<PlanStepResponse> steps = flattenSteps(response);
        assertFalse(steps.isEmpty(), "План должен содержать шаги");

        Set<Long> roleTopicIds = roleTopics.stream()
                .map(roleTopic -> roleTopic.getTopic().getId())
                .collect(Collectors.toSet());
        Set<Long> actualTopicIds = new LinkedHashSet<>();

        for (PlanStepResponse step : steps) {
            assertTrue(roleTopicIds.contains(step.topicId()),
                    "Тема '%s' не принадлежит выбранному roadmap".formatted(step.topicCode()));
            assertFalse(knownTopicIds.contains(step.topicId()),
                    "Известная тема '%s' не должна попадать в weekly plan".formatted(step.topicCode()));
            assertTrue(actualTopicIds.add(step.topicId()),
                    "Тема '%s' не должна дублироваться в плане".formatted(step.topicCode()));
            assertTrue(step.plannedHours().compareTo(BigDecimal.ZERO) > 0,
                    "У темы '%s' должны быть положительные часы".formatted(step.topicCode()));

            PlanStepExplanationResponse explanation = step.explanation();
            assertNotNull(explanation, "У шага '%s' должно быть explanation".formatted(step.topicCode()));
            for (PlanStepExplanationResponse.PrereqItem prereq : explanation.prereqs()) {
                assertNotEquals(PrereqStatus.MISSING, prereq.status(),
                        "У темы '%s' отсутствует обязательный prerequisite '%s'"
                                .formatted(step.topicCode(), prereq.prereqTopicCode()));
            }
        }

        assertAllWeeksRespectBudget(response.weeks(), hoursPerWeek);
    }

    private void assertAllWeeksRespectBudget(List<PlanWeekResponse> weeks, int hoursPerWeek) {
        BigDecimal budget = BigDecimal.valueOf(hoursPerWeek);
        for (PlanWeekResponse week : weeks) {
            assertTrue(week.hoursPlanned().compareTo(budget) <= 0,
                    "Неделя %s превышает лимит часов".formatted(week.weekIndex()));
            assertEquals(budget, week.hoursBudget(),
                    "У недели %s должен сохраняться исходный недельный лимит".formatted(week.weekIndex()));
        }
    }

    private List<PlanStepResponse> flattenSteps(PlanFullResponse response) {
        return response.weeks().stream()
                .flatMap(week -> week.steps().stream())
                .toList();
    }

    private Set<Long> firstRequiredTopicIds(List<RoleTopic> roleTopics, int limit) {
        return roleTopics.stream()
                .filter(RoleTopic::isRequired)
                .limit(limit)
                .map(roleTopic -> roleTopic.getTopic().getId())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private int safeWeeklyBudget(List<RoleTopic> roleTopics) {
        int maxTopicHours = roleTopics.stream()
                .map(RoleTopic::getTopic)
                .map(topic -> topic.getEstimatedHours() != null ? topic.getEstimatedHours() : BigDecimal.ONE)
                .map(hours -> hours.setScale(0, RoundingMode.CEILING).intValueExact())
                .max(Integer::compareTo)
                .orElse(1);

        return Math.max(maxTopicHours, 8);
    }

    private RoleGoal getActiveRole(String code) {
        RoleGoal role = roleGoalRepository.findByCode(code)
                .orElseThrow(() -> new AssertionError("Roadmap '%s' не найден в KB".formatted(code)));
        assertEquals(EntityStatus.ACTIVE, role.getStatus(),
                "Roadmap '%s' должен быть ACTIVE для пользовательского каталога".formatted(code));
        return role;
    }

    private User createVerificationUser() {
        return userRepository.save(User.builder()
                .email("verification+" + UUID.randomUUID() + "@example.com")
                .passwordHash("verification")
                .displayName("Verification")
                .createdAt(Instant.now())
                .build());
    }
}
