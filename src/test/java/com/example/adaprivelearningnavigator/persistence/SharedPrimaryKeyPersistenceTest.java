package com.example.adaprivelearningnavigator.persistence;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.planWhatIf.Plan;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanParamsSnapshot;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStep;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepExplanation;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanWeek;
import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.PlanParamsSnapshotRepository;
import com.example.adaprivelearningnavigator.repo.PlanRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepExplanationRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepRepository;
import com.example.adaprivelearningnavigator.repo.PlanWeekRepository;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class SharedPrimaryKeyPersistenceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleGoalRepository roleGoalRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanParamsSnapshotRepository planParamsSnapshotRepository;

    @Autowired
    private PlanWeekRepository planWeekRepository;

    @Autowired
    private PlanStepRepository planStepRepository;

    @Autowired
    private PlanStepExplanationRepository planStepExplanationRepository;

    @Test
    void shouldPersistPlanParamsSnapshotUsingMapsId() {
        Plan plan = persistPlan();

        PlanParamsSnapshot snapshot = PlanParamsSnapshot.builder()
                .plan(plan)
                .hoursPerWeek(10)
                .prefsLanguage("ru")
                .prefsResourceTypes("VIDEO")
                .algoVersion("ai-ollama-v1")
                .build();

        PlanParamsSnapshot saved = planParamsSnapshotRepository.saveAndFlush(snapshot);

        assertEquals(plan.getId(), saved.getPlanId());
        assertTrue(planParamsSnapshotRepository.findById(plan.getId()).isPresent());
    }

    @Test
    void shouldPersistPlanStepExplanationUsingMapsId() {
        Plan plan = persistPlan();
        Topic topic = topicRepository.saveAndFlush(Topic.builder()
                .code("SPRING_BOOT")
                .title("Spring Boot")
                .description("Spring Boot basics")
                .level(TopicLevel.INTERMEDIATE)
                .core(true)
                .estimatedHours(BigDecimal.valueOf(8))
                .status(EntityStatus.ACTIVE)
                .build());

        PlanWeek week = planWeekRepository.saveAndFlush(PlanWeek.builder()
                .plan(plan)
                .weekIndex(1)
                .hoursBudget(BigDecimal.TEN)
                .hoursPlanned(BigDecimal.ZERO)
                .build());

        PlanStep step = planStepRepository.saveAndFlush(PlanStep.builder()
                .planWeek(week)
                .topic(topic)
                .orderInWeek(1)
                .plannedHours(BigDecimal.valueOf(8))
                .optional(false)
                .build());

        PlanStepExplanation explanation = PlanStepExplanation.builder()
                .planStep(step)
                .ruleApplied("AI_LED_ROUTE_BACKEND_VALIDATED")
                .topicPriorityReason("AI suggested this topic")
                .resourceReason(null)
                .build();

        PlanStepExplanation saved = planStepExplanationRepository.saveAndFlush(explanation);

        assertEquals(step.getId(), saved.getPlanStepId());
        assertTrue(planStepExplanationRepository.findById(step.getId()).isPresent());
    }

    private Plan persistPlan() {
        User user = userRepository.saveAndFlush(User.builder()
                .email("mapsid-" + System.nanoTime() + "@example.com")
                .passwordHash("hash")
                .displayName("MapsId Test")
                .createdAt(Instant.now())
                .build());

        RoleGoal roleGoal = roleGoalRepository.saveAndFlush(RoleGoal.builder()
                .code("mapsid-role-" + System.nanoTime())
                .name("MapsId Role")
                .description("Role for MapsId test")
                .status(EntityStatus.ACTIVE)
                .build());

        return planRepository.saveAndFlush(Plan.builder()
                .user(user)
                .role(roleGoal)
                .status(EntityStatus.DRAFT.name())
                .scenarioType(ScenarioType.BASE)
                .scenarioLabel(null)
                .build());
    }
}
