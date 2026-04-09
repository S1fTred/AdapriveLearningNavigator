package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.ai.dto.AiGeneratedTopicDto;
import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.ai.service.AiRouteGenerationService;
import com.example.adaprivelearningnavigator.ai.validation.AiRouteValidationService;
import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepExplanationPrereqId;
import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepResourceId;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import com.example.adaprivelearningnavigator.domain.enums.PrereqStatus;
import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Resource;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import com.example.adaprivelearningnavigator.domain.planWhatIf.Plan;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanParamsSnapshot;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStep;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepExplanation;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepExplanationPrereq;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepResource;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanWeek;
import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.PlanParamsSnapshotRepository;
import com.example.adaprivelearningnavigator.repo.PlanRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepExplanationPrereqRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepExplanationRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepRepository;
import com.example.adaprivelearningnavigator.repo.PlanStepResourceRepository;
import com.example.adaprivelearningnavigator.repo.PlanWeekRepository;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicPrereqRepository;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanShortResponse;
import com.example.adaprivelearningnavigator.service.exception.AiRouteValidationException;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import com.example.adaprivelearningnavigator.service.exception.PlanBuildException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleGoalRepository roleGoalRepository;
    @Mock private RoleTopicRepository roleTopicRepository;
    @Mock private TopicPrereqRepository topicPrereqRepository;
    @Mock private PlanRepository planRepository;
    @Mock private PlanParamsSnapshotRepository planParamsSnapshotRepository;
    @Mock private PlanWeekRepository planWeekRepository;
    @Mock private PlanStepRepository planStepRepository;
    @Mock private PlanStepExplanationRepository planStepExplanationRepository;
    @Mock private PlanStepExplanationPrereqRepository planStepExplanationPrereqRepository;
    @Mock private PlanStepResourceRepository planStepResourceRepository;
    @Mock private AiRouteGenerationService aiRouteGenerationService;
    @Mock private AiRouteValidationService aiRouteValidationService;

    @Test
    void shouldExcludeKnownTopicsAndAddMissingPrerequisites() {
        User user = user(1L, "test@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");
        Topic basics = topic(10L, "JAVA_BASICS", "Java Basics");
        Topic http = topic(11L, "HTTP", "HTTP");
        Topic spring = topic(12L, "SPRING_BOOT", "Spring Boot");

        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "java-backend",
                UserLevel.BEGINNER,
                8,
                Set.of("Java Basics")
        );
        AiRouteGenerateResponse aiResponse = new AiRouteGenerateResponse(
                "Java backend developer",
                List.of(
                        generatedTopic("Java Basics", 1, 2, "Known base"),
                        generatedTopic("Spring Boot", 2, 6, "Main goal topic")
                )
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleGoalRepository.findAll()).thenReturn(List.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(100L))
                .thenReturn(List.of(roleTopic(role, spring, 1, true)));
        when(topicPrereqRepository.findAll()).thenReturn(List.of(
                prereq(basics, spring),
                prereq(http, spring)
        ));
        when(aiRouteGenerationService.generateRoute(eq(request), anyList())).thenReturn(aiResponse);
        doNothing().when(aiRouteValidationService).validateGeneratedRoute(aiResponse);
        when(aiRouteValidationService.resolveExistingTopic("Java Basics")).thenReturn(basics);
        when(aiRouteValidationService.resolveExistingTopic(any(AiGeneratedTopicDto.class))).thenAnswer(invocation -> {
            AiGeneratedTopicDto dto = invocation.getArgument(0);
            return switch (dto.topicCode()) {
                case "JAVA_BASICS" -> basics;
                case "SPRING_BOOT" -> spring;
                default -> null;
            };
        });

        List<PlanStep> savedSteps = new ArrayList<>();
        List<PlanStepExplanationPrereq> savedPrereqs = new ArrayList<>();
        PlanParamsSnapshot[] savedSnapshot = new PlanParamsSnapshot[1];
        PlanServiceImpl planService = spyPlanService(savedSteps, savedPrereqs, savedSnapshot);

        PlanFullResponse response = planService.generatePlanWithAi(1L, request);

        assertEquals(500L, response.id());
        assertEquals("ai-ollama-v1", savedSnapshot[0].getAlgoVersion());
        assertEquals(2, savedSteps.size());
        assertIterableEquals(
                List.of(http.getId(), spring.getId()),
                savedSteps.stream().map(step -> step.getTopic().getId()).toList()
        );

        List<PlanStepExplanationPrereq> springPrereqs = savedPrereqs.stream()
                .filter(item -> item.getId().getPlanStepId().equals(savedSteps.get(1).getId()))
                .toList();

        assertEquals(2, springPrereqs.size());
        assertEquals(
                PrereqStatus.DONE,
                statusForTopic(springPrereqs, basics.getId())
        );
        assertEquals(
                PrereqStatus.IN_PREVIOUS_STEPS,
                statusForTopic(springPrereqs, http.getId())
        );
    }

    @Test
    void shouldFailWhenTopicDoesNotFitWeeklyBudget() {
        User user = user(1L, "test@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");
        Topic spring = topic(12L, "SPRING_BOOT", "Spring Boot");

        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "java-backend",
                UserLevel.BEGINNER,
                5,
                Set.of()
        );
        AiRouteGenerateResponse aiResponse = new AiRouteGenerateResponse(
                "Java backend developer",
                List.of(generatedTopic("Spring Boot", 1, 12, "Too large topic"))
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleGoalRepository.findAll()).thenReturn(List.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(100L))
                .thenReturn(List.of(roleTopic(role, spring, 1, true)));
        when(topicPrereqRepository.findAll()).thenReturn(List.of());
        when(aiRouteGenerationService.generateRoute(eq(request), anyList())).thenReturn(aiResponse);
        doNothing().when(aiRouteValidationService).validateGeneratedRoute(aiResponse);
        when(aiRouteValidationService.resolveExistingTopic(any(AiGeneratedTopicDto.class))).thenReturn(spring);

        PlanServiceImpl planService = spyPlanService(new ArrayList<>(), new ArrayList<>(), new PlanParamsSnapshot[1]);

        assertThrows(PlanBuildException.class, () -> planService.generatePlanWithAi(1L, request));
    }

    @Test
    void shouldFailWhenRoleResolutionIsAmbiguous() {
        User user = user(1L, "test@example.com");
        RoleGoal first = roleGoal(100L, "java-backend", "Java Backend Developer");
        RoleGoal second = roleGoal(101L, "java-mobile", "Java Mobile Developer");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleGoalRepository.findAll()).thenReturn(List.of(first, second));

        PlanServiceImpl planService = spyPlanService(new ArrayList<>(), new ArrayList<>(), new PlanParamsSnapshot[1]);

        assertThrows(AiRouteValidationException.class, () -> planService.generatePlanWithAi(
                1L,
                new AiPlanGenerateRequest("java", UserLevel.BEGINNER, 8, Set.of())
        ));
    }

    @Test
    void shouldFailWhenAllAiTopicsAreAlreadyKnown() {
        User user = user(1L, "test@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");
        Topic basics = topic(10L, "JAVA_BASICS", "Java Basics");

        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "java-backend",
                UserLevel.BEGINNER,
                8,
                Set.of("Java Basics")
        );
        AiRouteGenerateResponse aiResponse = new AiRouteGenerateResponse(
                "Java backend developer",
                List.of(generatedTopic("Java Basics", 1, 2, "Already known"))
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleGoalRepository.findAll()).thenReturn(List.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(100L))
                .thenReturn(List.of(roleTopic(role, basics, 1, true)));
        when(topicPrereqRepository.findAll()).thenReturn(List.of());
        when(aiRouteGenerationService.generateRoute(eq(request), anyList())).thenReturn(aiResponse);
        doNothing().when(aiRouteValidationService).validateGeneratedRoute(aiResponse);
        when(aiRouteValidationService.resolveExistingTopic("Java Basics")).thenReturn(basics);
        when(aiRouteValidationService.resolveExistingTopic(any(AiGeneratedTopicDto.class))).thenReturn(basics);

        PlanServiceImpl planService = spyPlanService(new ArrayList<>(), new ArrayList<>(), new PlanParamsSnapshot[1]);

        assertThrows(PlanBuildException.class, () -> planService.generatePlanWithAi(1L, request));
    }

    @Test
    void shouldResolveRoleByExactName() {
        User user = user(1L, "test@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");
        Topic spring = topic(12L, "SPRING_BOOT", "Spring Boot");

        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "Java Backend Developer",
                UserLevel.BEGINNER,
                8,
                Set.of()
        );
        AiRouteGenerateResponse aiResponse = new AiRouteGenerateResponse(
                "Java backend developer",
                List.of(generatedTopic("Spring Boot", 1, 6, "Main goal topic"))
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleGoalRepository.findAll()).thenReturn(List.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(100L))
                .thenReturn(List.of(roleTopic(role, spring, 1, true)));
        when(topicPrereqRepository.findAll()).thenReturn(List.of());
        when(aiRouteGenerationService.generateRoute(eq(request), anyList())).thenReturn(aiResponse);
        doNothing().when(aiRouteValidationService).validateGeneratedRoute(aiResponse);
        when(aiRouteValidationService.resolveExistingTopic(any(AiGeneratedTopicDto.class))).thenReturn(spring);

        PlanServiceImpl planService = spyPlanService(new ArrayList<>(), new ArrayList<>(), new PlanParamsSnapshot[1]);

        PlanFullResponse response = planService.generatePlanWithAi(1L, request);

        assertEquals(500L, response.id());
    }

    @Test
    void shouldResolveRoleByContainsMatch() {
        User user = user(1L, "test@example.com");
        RoleGoal matchingRole = roleGoal(100L, "java-backend", "Java Backend Developer");
        RoleGoal otherRole = roleGoal(101L, "frontend", "Frontend Developer");
        Topic spring = topic(12L, "SPRING_BOOT", "Spring Boot");

        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "backend",
                UserLevel.BEGINNER,
                8,
                Set.of()
        );
        AiRouteGenerateResponse aiResponse = new AiRouteGenerateResponse(
                "Java backend developer",
                List.of(generatedTopic("Spring Boot", 1, 6, "Main goal topic"))
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleGoalRepository.findAll()).thenReturn(List.of(matchingRole, otherRole));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(100L))
                .thenReturn(List.of(roleTopic(matchingRole, spring, 1, true)));
        when(topicPrereqRepository.findAll()).thenReturn(List.of());
        when(aiRouteGenerationService.generateRoute(eq(request), anyList())).thenReturn(aiResponse);
        doNothing().when(aiRouteValidationService).validateGeneratedRoute(aiResponse);
        when(aiRouteValidationService.resolveExistingTopic(any(AiGeneratedTopicDto.class))).thenReturn(spring);

        PlanServiceImpl planService = spyPlanService(new ArrayList<>(), new ArrayList<>(), new PlanParamsSnapshot[1]);

        PlanFullResponse response = planService.generatePlanWithAi(1L, request);

        assertEquals(500L, response.id());
    }

    @Test
    void shouldReturnPaginatedPlans() {
        User user = user(1L, "test@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");

        Plan first = plan(700L, user, role, ScenarioType.BASE, "ACTIVE");
        first.setCreatedAt(Instant.parse("2026-04-09T10:00:00Z"));
        Plan second = plan(701L, user, role, ScenarioType.WHAT_IF, "DRAFT");
        second.setCreatedAt(Instant.parse("2026-04-08T10:00:00Z"));
        Plan third = plan(702L, user, role, ScenarioType.BASE, "ARCHIVED");
        third.setCreatedAt(Instant.parse("2026-04-07T10:00:00Z"));

        when(planRepository.findAllByUser_IdOrderByCreatedAtDesc(1L)).thenReturn(List.of(first, second, third));

        PlanServiceImpl planService = new PlanServiceImpl(
                userRepository,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository,
                planRepository,
                planParamsSnapshotRepository,
                planWeekRepository,
                planStepRepository,
                planStepExplanationRepository,
                planStepExplanationPrereqRepository,
                planStepResourceRepository,
                aiRouteGenerationService,
                aiRouteValidationService
        );

        PageResponse<PlanShortResponse> page = planService.getPlans(1L, 1, 2);

        assertEquals(3, page.total());
        assertEquals(1, page.page());
        assertEquals(2, page.size());
        assertEquals(1, page.items().size());
        assertEquals(702L, page.items().get(0).id());
        assertEquals(ScenarioType.BASE, page.items().get(0).scenarioType());
    }

    @Test
    void shouldReturnFullPlanWithWeeksStepsAndExplanation() {
        User user = user(1L, "test@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");
        Plan plan = plan(500L, user, role, ScenarioType.BASE, "DRAFT");
        plan.setCreatedAt(Instant.parse("2026-04-09T10:00:00Z"));

        Topic topic = topic(10L, "SPRING_BOOT", "Spring Boot");
        Topic prereqTopic = topic(11L, "HTTP", "HTTP");
        Resource resource = Resource.builder()
                .id(900L)
                .title("Spring Course")
                .url("https://example.com/spring")
                .status(EntityStatus.ACTIVE)
                .build();

        PlanParamsSnapshot snapshot = PlanParamsSnapshot.builder()
                .planId(plan.getId())
                .plan(plan)
                .hoursPerWeek(8)
                .prefsLanguage("ru")
                .prefsResourceTypes("VIDEO")
                .algoVersion("ai-ollama-v1")
                .build();

        PlanWeek week = PlanWeek.builder()
                .id(600L)
                .plan(plan)
                .weekIndex(1)
                .hoursBudget(BigDecimal.valueOf(8))
                .hoursPlanned(BigDecimal.valueOf(6))
                .build();

        PlanStep step = PlanStep.builder()
                .id(700L)
                .planWeek(week)
                .topic(topic)
                .orderInWeek(1)
                .plannedHours(BigDecimal.valueOf(6))
                .optional(false)
                .build();

        PlanStepExplanation explanation = PlanStepExplanation.builder()
                .planStepId(step.getId())
                .planStep(step)
                .ruleApplied("AI_LED_ROUTE_BACKEND_VALIDATED")
                .topicPriorityReason("AI suggested")
                .resourceReason(null)
                .build();

        PlanStepResource stepResource = PlanStepResource.builder()
                .id(new PlanStepResourceId(step.getId(), resource.getId()))
                .planStep(step)
                .resource(resource)
                .primary(true)
                .build();

        PlanStepExplanationPrereq prereq = PlanStepExplanationPrereq.builder()
                .id(new PlanStepExplanationPrereqId(step.getId(), prereqTopic.getId()))
                .explanation(explanation)
                .prereqTopic(prereqTopic)
                .prereqStatus(PrereqStatus.DONE)
                .build();

        when(planRepository.findById(500L)).thenReturn(Optional.of(plan));
        when(planParamsSnapshotRepository.findById(500L)).thenReturn(Optional.of(snapshot));
        when(planWeekRepository.findAllByPlan_IdOrderByWeekIndexAsc(500L)).thenReturn(List.of(week));
        when(planStepRepository.findAllByPlanWeek_IdOrderByOrderInWeekAsc(600L)).thenReturn(List.of(step));
        when(planStepResourceRepository.findAllByPlanStep_Id(700L)).thenReturn(List.of(stepResource));
        when(planStepExplanationRepository.findById(700L)).thenReturn(Optional.of(explanation));
        when(planStepExplanationPrereqRepository.findAllByExplanation_PlanStepId(700L)).thenReturn(List.of(prereq));

        PlanServiceImpl planService = new PlanServiceImpl(
                userRepository,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository,
                planRepository,
                planParamsSnapshotRepository,
                planWeekRepository,
                planStepRepository,
                planStepExplanationRepository,
                planStepExplanationPrereqRepository,
                planStepResourceRepository,
                aiRouteGenerationService,
                aiRouteValidationService
        );

        PlanFullResponse response = planService.getPlan(1L, 500L);

        assertEquals(500L, response.id());
        assertNotNull(response.params());
        assertEquals("ai-ollama-v1", response.params().algoVersion());
        assertEquals(1, response.weeks().size());
        assertEquals(1, response.weeks().get(0).steps().size());
        assertEquals(900L, response.weeks().get(0).steps().get(0).resources().get(0).resourceId());
        assertEquals("AI_LED_ROUTE_BACKEND_VALIDATED", response.weeks().get(0).steps().get(0).explanation().ruleApplied());
        assertEquals(PrereqStatus.DONE, response.weeks().get(0).steps().get(0).explanation().prereqs().get(0).status());
    }

    @Test
    void shouldRejectAccessToForeignPlan() {
        User owner = user(2L, "owner@example.com");
        User requestedUser = user(1L, "requester@example.com");
        RoleGoal role = roleGoal(100L, "java-backend", "Java Backend Developer");
        Plan foreignPlan = plan(500L, owner, role, ScenarioType.BASE, "DRAFT");

        when(planRepository.findById(500L)).thenReturn(Optional.of(foreignPlan));

        PlanServiceImpl planService = new PlanServiceImpl(
                userRepository,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository,
                planRepository,
                planParamsSnapshotRepository,
                planWeekRepository,
                planStepRepository,
                planStepExplanationRepository,
                planStepExplanationPrereqRepository,
                planStepResourceRepository,
                aiRouteGenerationService,
                aiRouteValidationService
        );

        assertThrows(NotFoundException.class, () -> planService.getPlan(requestedUser.getId(), 500L));
    }

    @Test
    void shouldFailClassicBuildPlanUntilImplemented() {
        PlanServiceImpl planService = new PlanServiceImpl(
                userRepository,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository,
                planRepository,
                planParamsSnapshotRepository,
                planWeekRepository,
                planStepRepository,
                planStepExplanationRepository,
                planStepExplanationPrereqRepository,
                planStepResourceRepository,
                aiRouteGenerationService,
                aiRouteValidationService
        );

        assertThrows(PlanBuildException.class, () -> planService.buildPlan(1L, null));
    }

    @Test
    void shouldFailWhatIfBuildUntilImplemented() {
        PlanServiceImpl planService = new PlanServiceImpl(
                userRepository,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository,
                planRepository,
                planParamsSnapshotRepository,
                planWeekRepository,
                planStepRepository,
                planStepExplanationRepository,
                planStepExplanationPrereqRepository,
                planStepResourceRepository,
                aiRouteGenerationService,
                aiRouteValidationService
        );

        assertThrows(PlanBuildException.class, () -> planService.buildWhatIfPlan(1L, 10L, null));
    }

    private PlanServiceImpl spyPlanService(List<PlanStep> savedSteps,
                                           List<PlanStepExplanationPrereq> savedPrereqs,
                                           PlanParamsSnapshot[] savedSnapshot) {
        PlanServiceImpl planService = spy(new PlanServiceImpl(
                userRepository,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository,
                planRepository,
                planParamsSnapshotRepository,
                planWeekRepository,
                planStepRepository,
                planStepExplanationRepository,
                planStepExplanationPrereqRepository,
                planStepResourceRepository,
                aiRouteGenerationService,
                aiRouteValidationService
        ));

        AtomicLong planIds = new AtomicLong(500L);
        AtomicLong weekIds = new AtomicLong(600L);
        AtomicLong stepIds = new AtomicLong(700L);

        lenient().when(planRepository.save(any(Plan.class))).thenAnswer(invocation -> {
            Plan plan = invocation.getArgument(0);
            if (plan.getId() == null) {
                plan.setId(planIds.getAndIncrement());
            }
            return plan;
        });
        lenient().when(planParamsSnapshotRepository.save(any(PlanParamsSnapshot.class))).thenAnswer(invocation -> {
            PlanParamsSnapshot snapshot = invocation.getArgument(0);
            savedSnapshot[0] = snapshot;
            return snapshot;
        });
        lenient().when(planWeekRepository.save(any(PlanWeek.class))).thenAnswer(invocation -> {
            PlanWeek week = invocation.getArgument(0);
            if (week.getId() == null) {
                week.setId(weekIds.getAndIncrement());
            }
            return week;
        });
        lenient().when(planStepRepository.save(any(PlanStep.class))).thenAnswer(invocation -> {
            PlanStep step = invocation.getArgument(0);
            if (step.getId() == null) {
                step.setId(stepIds.getAndIncrement());
            }
            savedSteps.add(step);
            return step;
        });
        lenient().when(planStepExplanationRepository.save(any(PlanStepExplanation.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(planStepExplanationPrereqRepository.save(any(PlanStepExplanationPrereq.class))).thenAnswer(invocation -> {
            PlanStepExplanationPrereq prereq = invocation.getArgument(0);
            savedPrereqs.add(prereq);
            return prereq;
        });
        lenient().doAnswer(invocation -> PlanFullResponse.builder()
                .id(invocation.getArgument(1, Long.class))
                .weeks(List.of())
                .build())
                .when(planService).getPlan(anyLong(), anyLong());

        return planService;
    }

    private PrereqStatus statusForTopic(List<PlanStepExplanationPrereq> items, Long topicId) {
        return items.stream()
                .filter(item -> item.getPrereqTopic().getId().equals(topicId))
                .map(PlanStepExplanationPrereq::getPrereqStatus)
                .findFirst()
                .orElseThrow();
    }

    private User user(Long id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .displayName("Test")
                .createdAt(Instant.now())
                .build();
    }

    private Plan plan(Long id, User user, RoleGoal role, ScenarioType scenarioType, String status) {
        Plan plan = Plan.builder()
                .id(id)
                .user(user)
                .role(role)
                .scenarioType(scenarioType)
                .status(status)
                .build();
        plan.setCreatedAt(Instant.now());
        return plan;
    }

    private RoleGoal roleGoal(Long id, String code, String name) {
        RoleGoal roleGoal = new RoleGoal();
        roleGoal.setId(id);
        roleGoal.setCode(code);
        roleGoal.setName(name);
        return roleGoal;
    }

    private Topic topic(Long id, String code, String title) {
        Topic topic = new Topic();
        topic.setId(id);
        topic.setCode(code);
        topic.setTitle(title);
        return topic;
    }

    private RoleTopic roleTopic(RoleGoal role, Topic topic, Integer priority, boolean required) {
        RoleTopic roleTopic = new RoleTopic();
        roleTopic.setRole(role);
        roleTopic.setTopic(topic);
        roleTopic.setPriority(priority);
        roleTopic.setRequired(required);
        return roleTopic;
    }

    private TopicPrereq prereq(Topic prereqTopic, Topic nextTopic) {
        TopicPrereq topicPrereq = new TopicPrereq();
        topicPrereq.setPrereqTopic(prereqTopic);
        topicPrereq.setNextTopic(nextTopic);
        topicPrereq.setRelationType(PrereqRelationType.REQUIRED);
        return topicPrereq;
    }

    private AiGeneratedTopicDto generatedTopic(String title, int priority, int hours, String reason) {
        return new AiGeneratedTopicDto(
                title.toUpperCase(Locale.ROOT).replace(' ', '_').replace('/', '_').replace('-', '_'),
                title,
                priority,
                BigDecimal.valueOf(hours),
                reason,
                List.of()
        );
    }
}
