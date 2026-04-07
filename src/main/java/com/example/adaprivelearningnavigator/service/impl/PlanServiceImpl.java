package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.ai.dto.AiGeneratedTopicDto;
import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.ai.service.AiRouteGenerationService;
import com.example.adaprivelearningnavigator.ai.validation.AiRouteValidationService;
import com.example.adaprivelearningnavigator.domain.compositeKeys.PlanStepExplanationPrereqId;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import com.example.adaprivelearningnavigator.domain.enums.PrereqStatus;
import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import com.example.adaprivelearningnavigator.domain.planWhatIf.Plan;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanParamsSnapshot;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStep;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepExplanation;
import com.example.adaprivelearningnavigator.domain.planWhatIf.PlanStepExplanationPrereq;
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
import com.example.adaprivelearningnavigator.service.PlanService;
import com.example.adaprivelearningnavigator.service.dto.common.IdResponse;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanBuildRequest;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanParamsSnapshotResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanShortResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanStepExplanationResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanStepResourceResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanStepResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanWeekResponse;
import com.example.adaprivelearningnavigator.service.exception.AiRouteValidationException;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import com.example.adaprivelearningnavigator.service.exception.PlanBuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private static final Logger log = LoggerFactory.getLogger(PlanServiceImpl.class);
    private static final String AI_ALGO_VERSION = "ai-ollama-v1";

    private final UserRepository userRepository;
    private final RoleGoalRepository roleGoalRepository;
    private final RoleTopicRepository roleTopicRepository;
    private final TopicPrereqRepository topicPrereqRepository;
    private final PlanRepository planRepository;
    private final PlanParamsSnapshotRepository planParamsSnapshotRepository;
    private final PlanWeekRepository planWeekRepository;
    private final PlanStepRepository planStepRepository;
    private final PlanStepExplanationRepository planStepExplanationRepository;
    private final PlanStepExplanationPrereqRepository planStepExplanationPrereqRepository;
    private final PlanStepResourceRepository planStepResourceRepository;
    private final AiRouteGenerationService aiRouteGenerationService;
    private final AiRouteValidationService aiRouteValidationService;

    public PlanServiceImpl(UserRepository userRepository,
                           RoleGoalRepository roleGoalRepository,
                           RoleTopicRepository roleTopicRepository,
                           TopicPrereqRepository topicPrereqRepository,
                           PlanRepository planRepository,
                           PlanParamsSnapshotRepository planParamsSnapshotRepository,
                           PlanWeekRepository planWeekRepository,
                           PlanStepRepository planStepRepository,
                           PlanStepExplanationRepository planStepExplanationRepository,
                           PlanStepExplanationPrereqRepository planStepExplanationPrereqRepository,
                           PlanStepResourceRepository planStepResourceRepository,
                           AiRouteGenerationService aiRouteGenerationService,
                           AiRouteValidationService aiRouteValidationService) {
        this.userRepository = userRepository;
        this.roleGoalRepository = roleGoalRepository;
        this.roleTopicRepository = roleTopicRepository;
        this.topicPrereqRepository = topicPrereqRepository;
        this.planRepository = planRepository;
        this.planParamsSnapshotRepository = planParamsSnapshotRepository;
        this.planWeekRepository = planWeekRepository;
        this.planStepRepository = planStepRepository;
        this.planStepExplanationRepository = planStepExplanationRepository;
        this.planStepExplanationPrereqRepository = planStepExplanationPrereqRepository;
        this.planStepResourceRepository = planStepResourceRepository;
        this.aiRouteGenerationService = aiRouteGenerationService;
        this.aiRouteValidationService = aiRouteValidationService;
    }

    @Override
    public PlanFullResponse generatePlanWithAi(Long userId, AiPlanGenerateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        RoleGoal roleGoal = resolveRoleGoal(request.goal());
        log.info("Запуск AI-построения плана: userId={}, goal='{}', roleId={}", userId, request.goal(), roleGoal.getId());

        Set<Long> knownTopicIds = resolveKnownTopicIds(request.knownTopics());
        Map<Long, RoleTopic> roleTopicMeta = roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(roleGoal.getId())
                .stream()
                .collect(Collectors.toMap(roleTopic -> roleTopic.getTopic().getId(), Function.identity()));

        Map<Long, Topic> roleScopeTopics = buildRoleScope(roleTopicMeta.values());
        AiRouteGenerateResponse aiResponse = aiRouteGenerationService.generateRoute(request);
        aiRouteValidationService.validateGeneratedRoute(aiResponse);

        List<PlanningTopic> aiTopics = mapAiTopics(aiResponse, roleScopeTopics, roleTopicMeta, knownTopicIds);
        List<PlanningTopic> workingTopics = buildWorkingTopics(aiTopics, roleScopeTopics, knownTopicIds, roleTopicMeta);
        List<PlanningTopic> orderedTopics = orderTopics(workingTopics, roleTopicMeta);
        if (orderedTopics.isEmpty()) {
            throw new PlanBuildException("После исключения уже известных тем не осталось шагов для построения плана");
        }

        Plan plan = savePlan(user, roleGoal);
        savePlanSnapshot(plan, request.hoursPerWeek());
        savePlanWeeksAndSteps(plan, orderedTopics, request.hoursPerWeek(), knownTopicIds);

        log.info("AI-план сохранён: userId={}, planId={}, roleId={}", userId, plan.getId(), roleGoal.getId());
        return getPlan(userId, plan.getId());
    }

    @Override
    public IdResponse buildPlan(Long userId, PlanBuildRequest request) {
        throw new PlanBuildException("Обычная генерация плана пока не реализована");
    }

    @Override
    public IdResponse buildWhatIfPlan(Long userId, Long basePlanId, PlanBuildRequest request) {
        throw new PlanBuildException("What-if генерация плана пока не реализована");
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PlanShortResponse> getPlans(Long userId, int page, int size) {
        List<Plan> plans = planRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        int fromIndex = Math.min(safePage * safeSize, plans.size());
        int toIndex = Math.min(fromIndex + safeSize, plans.size());

        List<PlanShortResponse> items = plans.subList(fromIndex, toIndex).stream()
                .map(this::toShortResponse)
                .toList();

        return PageResponse.<PlanShortResponse>builder()
                .items(items)
                .total(plans.size())
                .page(safePage)
                .size(safeSize)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanFullResponse getPlan(Long userId, Long planId) {
        Plan plan = planRepository.findById(planId)
                .filter(value -> value.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("План не найден"));

        Optional<PlanParamsSnapshot> snapshotOpt = planParamsSnapshotRepository.findById(planId);
        List<PlanWeek> weeks = planWeekRepository.findAllByPlan_IdOrderByWeekIndexAsc(planId);

        List<PlanWeekResponse> weekResponses = weeks.stream()
                .map(this::toWeekResponse)
                .toList();

        return PlanFullResponse.builder()
                .id(plan.getId())
                .roleId(plan.getRole().getId())
                .status(plan.getStatus())
                .scenarioType(plan.getScenarioType())
                .scenarioLabel(plan.getScenarioLabel())
                .basePlanId(plan.getBasePlan() != null ? plan.getBasePlan().getId() : null)
                .createdAt(plan.getCreatedAt())
                .params(snapshotOpt.map(this::toSnapshotResponse).orElse(null))
                .weeks(weekResponses)
                .build();
    }

    private RoleGoal resolveRoleGoal(String rawGoal) {
        String goal = normalize(rawGoal);
        List<RoleGoal> roleGoals = roleGoalRepository.findAll();

        RoleGoal exactCode = singleOrNull(roleGoals.stream()
                .filter(role -> normalize(role.getCode()).equals(goal))
                .toList(), "Найдено несколько ролей по коду");
        if (exactCode != null) {
            return exactCode;
        }

        RoleGoal exactName = singleOrNull(roleGoals.stream()
                .filter(role -> normalize(role.getName()).equals(goal))
                .toList(), "Найдено несколько ролей по названию");
        if (exactName != null) {
            return exactName;
        }

        RoleGoal contains = singleOrNull(roleGoals.stream()
                .filter(role -> normalize(role.getCode()).contains(goal) || normalize(role.getName()).contains(goal))
                .toList(), "Цель обучения неоднозначна, найдено несколько ролей");
        if (contains != null) {
            return contains;
        }

        throw new AiRouteValidationException("Не удалось определить цель обучения по запросу: " + rawGoal);
    }

    private Set<Long> resolveKnownTopicIds(Set<String> knownTopics) {
        Set<Long> topicIds = new HashSet<>();
        for (String knownTopic : knownTopics) {
            topicIds.add(aiRouteValidationService.resolveExistingTopic(knownTopic).getId());
        }
        return topicIds;
    }

    private Map<Long, Topic> buildRoleScope(Collection<RoleTopic> roleTopics) {
        Map<Long, Topic> result = roleTopics.stream()
                .map(RoleTopic::getTopic)
                .collect(Collectors.toMap(Topic::getId, Function.identity(), (left, right) -> left, LinkedHashMap::new));

        Map<Long, List<TopicPrereq>> prereqsByNextId = buildRequiredPrereqMap();
        Deque<Topic> stack = new ArrayDeque<>(result.values());

        while (!stack.isEmpty()) {
            Topic current = stack.pop();
            for (TopicPrereq prereq : prereqsByNextId.getOrDefault(current.getId(), List.of())) {
                Topic prereqTopic = prereq.getPrereqTopic();
                if (!result.containsKey(prereqTopic.getId())) {
                    result.put(prereqTopic.getId(), prereqTopic);
                    stack.push(prereqTopic);
                }
            }
        }

        return result;
    }

    private List<PlanningTopic> mapAiTopics(AiRouteGenerateResponse aiResponse,
                                            Map<Long, Topic> roleScopeTopics,
                                            Map<Long, RoleTopic> roleTopicMeta,
                                            Set<Long> knownTopicIds) {
        List<PlanningTopic> topics = new ArrayList<>();
        Set<Long> seenTopicIds = new HashSet<>();

        for (int index = 0; index < aiResponse.topics().size(); index++) {
            AiGeneratedTopicDto generatedTopic = aiResponse.topics().get(index);
            Topic topic = aiRouteValidationService.resolveExistingTopic(generatedTopic.title());

            if (knownTopicIds.contains(topic.getId())) {
                continue;
            }

            if (!roleScopeTopics.containsKey(topic.getId())) {
                throw new AiRouteValidationException("Тема не относится к выбранной цели: " + generatedTopic.title());
            }
            if (!seenTopicIds.add(topic.getId())) {
                throw new AiRouteValidationException("AI вернул несколько ссылок на одну и ту же тему: " + generatedTopic.title());
            }

            RoleTopic roleTopic = roleTopicMeta.get(topic.getId());
            boolean optional = roleTopic != null && !roleTopic.isRequired();

            topics.add(new PlanningTopic(
                    topic,
                    generatedTopic.estimatedHours(),
                    generatedTopic.priority(),
                    index,
                    generatedTopic.reason(),
                    optional,
                    true
            ));
        }

        return topics;
    }

    private List<PlanningTopic> buildWorkingTopics(List<PlanningTopic> aiTopics,
                                                   Map<Long, Topic> roleScopeTopics,
                                                   Set<Long> knownTopicIds,
                                                   Map<Long, RoleTopic> roleTopicMeta) {
        Map<Long, PlanningTopic> result = aiTopics.stream()
                .collect(Collectors.toMap(
                        planningTopic -> planningTopic.topic().getId(),
                        Function.identity(),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        Map<Long, List<TopicPrereq>> prereqsByNextId = buildRequiredPrereqMap();
        Deque<Topic> stack = aiTopics.stream()
                .map(PlanningTopic::topic)
                .collect(Collectors.toCollection(ArrayDeque::new));

        while (!stack.isEmpty()) {
            Topic current = stack.pop();
            for (TopicPrereq prereq : prereqsByNextId.getOrDefault(current.getId(), List.of())) {
                Topic prereqTopic = prereq.getPrereqTopic();
                if (knownTopicIds.contains(prereqTopic.getId()) || !roleScopeTopics.containsKey(prereqTopic.getId())) {
                    continue;
                }
                if (!result.containsKey(prereqTopic.getId())) {
                    RoleTopic roleTopic = roleTopicMeta.get(prereqTopic.getId());
                    boolean optional = roleTopic != null && !roleTopic.isRequired();
                    result.put(prereqTopic.getId(), new PlanningTopic(
                            prereqTopic,
                            defaultHours(prereqTopic),
                            Integer.MAX_VALUE,
                            Integer.MAX_VALUE,
                            "Добавлено backend как обязательный пререквизит.",
                            optional,
                            false
                    ));
                    stack.push(prereqTopic);
                }
            }
        }

        return new ArrayList<>(result.values());
    }

    private List<PlanningTopic> orderTopics(List<PlanningTopic> topics, Map<Long, RoleTopic> roleTopicMeta) {
        Map<Long, PlanningTopic> topicById = topics.stream()
                .collect(Collectors.toMap(planningTopic -> planningTopic.topic().getId(), Function.identity()));
        Map<Long, List<Long>> outgoing = new HashMap<>();
        Map<Long, Integer> indegree = new HashMap<>();
        Map<Long, List<TopicPrereq>> prereqsByNextId = buildRequiredPrereqMap();

        for (PlanningTopic topic : topics) {
            indegree.put(topic.topic().getId(), 0);
            outgoing.put(topic.topic().getId(), new ArrayList<>());
        }

        for (PlanningTopic topic : topics) {
            for (TopicPrereq prereq : prereqsByNextId.getOrDefault(topic.topic().getId(), List.of())) {
                Long prereqId = prereq.getPrereqTopic().getId();
                if (!topicById.containsKey(prereqId)) {
                    continue;
                }
                outgoing.get(prereqId).add(topic.topic().getId());
                indegree.compute(topic.topic().getId(), (key, value) -> value == null ? 1 : value + 1);
            }
        }

        Comparator<PlanningTopic> comparator = Comparator
                .comparing((PlanningTopic item) -> item.aiPriority(), Integer::compareTo)
                .thenComparing(PlanningTopic::aiOrder, Integer::compareTo)
                .thenComparing(item -> rolePriority(roleTopicMeta.get(item.topic().getId())), Integer::compareTo)
                .thenComparing(item -> item.optional() ? 1 : 0, Integer::compareTo)
                .thenComparing(item -> item.topic().isCore() ? 0 : 1, Integer::compareTo)
                .thenComparing(PlanningTopic::plannedHours)
                .thenComparing(item -> item.topic().getTitle(), String.CASE_INSENSITIVE_ORDER);

        PriorityQueue<PlanningTopic> queue = new PriorityQueue<>(comparator);
        for (PlanningTopic topic : topics) {
            if (indegree.getOrDefault(topic.topic().getId(), 0) == 0) {
                queue.add(topic);
            }
        }

        List<PlanningTopic> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            PlanningTopic current = queue.poll();
            ordered.add(current);

            for (Long dependentId : outgoing.getOrDefault(current.topic().getId(), List.of())) {
                int newIndegree = indegree.computeIfPresent(dependentId, (key, value) -> value - 1);
                if (newIndegree == 0) {
                    queue.add(topicById.get(dependentId));
                }
            }
        }

        if (ordered.size() != topics.size()) {
            throw new PlanBuildException("Не удалось построить корректный порядок тем из-за цикла в графе зависимостей");
        }

        return ordered;
    }

    private Plan savePlan(User user, RoleGoal roleGoal) {
        Plan plan = Plan.builder()
                .user(user)
                .role(roleGoal)
                .status(EntityStatus.DRAFT.name())
                .scenarioType(ScenarioType.BASE)
                .scenarioLabel(null)
                .build();
        return planRepository.save(plan);
    }

    private void savePlanSnapshot(Plan plan, Integer hoursPerWeek) {
        PlanParamsSnapshot snapshot = PlanParamsSnapshot.builder()
                .planId(plan.getId())
                .plan(plan)
                .hoursPerWeek(hoursPerWeek)
                .prefsLanguage(null)
                .prefsResourceTypes(null)
                .algoVersion(AI_ALGO_VERSION)
                .build();
        planParamsSnapshotRepository.save(snapshot);
    }

    private void savePlanWeeksAndSteps(Plan plan,
                                       List<PlanningTopic> orderedTopics,
                                       Integer hoursPerWeek,
                                       Set<Long> knownTopicIds) {
        BigDecimal weekBudget = BigDecimal.valueOf(hoursPerWeek);
        BigDecimal currentWeekHours = BigDecimal.ZERO;
        int weekIndex = 1;
        int orderInWeek = 1;
        PlanWeek currentWeek = createWeek(plan, weekIndex, weekBudget);
        Map<Long, Integer> topicOrder = new HashMap<>();
        int globalIndex = 0;

        for (PlanningTopic planningTopic : orderedTopics) {
            BigDecimal topicHours = planningTopic.plannedHours();
            if (topicHours.compareTo(weekBudget) > 0) {
                throw new PlanBuildException("Тема \"" + planningTopic.topic().getTitle()
                        + "\" не помещается в недельный лимит часов");
            }
            boolean shouldStartNewWeek = currentWeekHours.signum() > 0
                    && currentWeekHours.add(topicHours).compareTo(weekBudget) > 0;

            if (shouldStartNewWeek) {
                currentWeek.setHoursPlanned(currentWeekHours);
                planWeekRepository.save(currentWeek);
                weekIndex++;
                currentWeek = createWeek(plan, weekIndex, weekBudget);
                currentWeekHours = BigDecimal.ZERO;
                orderInWeek = 1;
            }

            currentWeekHours = currentWeekHours.add(topicHours);
            PlanStep step = planStepRepository.save(PlanStep.builder()
                    .planWeek(currentWeek)
                    .topic(planningTopic.topic())
                    .orderInWeek(orderInWeek++)
                    .plannedHours(topicHours)
                    .optional(planningTopic.optional())
                    .build());

            topicOrder.put(planningTopic.topic().getId(), globalIndex++);
            saveStepExplanation(step, planningTopic, knownTopicIds, topicOrder);
        }

        currentWeek.setHoursPlanned(currentWeekHours);
        planWeekRepository.save(currentWeek);
    }

    private PlanWeek createWeek(Plan plan, int weekIndex, BigDecimal budget) {
        return planWeekRepository.save(PlanWeek.builder()
                .plan(plan)
                .weekIndex(weekIndex)
                .hoursBudget(budget)
                .hoursPlanned(BigDecimal.ZERO)
                .build());
    }

    private void saveStepExplanation(PlanStep step,
                                     PlanningTopic planningTopic,
                                     Set<Long> knownTopicIds,
                                     Map<Long, Integer> topicOrder) {
        String ruleApplied = planningTopic.aiSuggested()
                ? "AI_LED_ROUTE_BACKEND_VALIDATED"
                : "KB_REQUIRED_PREREQ_ADDED";

        PlanStepExplanation explanation = planStepExplanationRepository.save(PlanStepExplanation.builder()
                .planStepId(step.getId())
                .planStep(step)
                .ruleApplied(ruleApplied)
                .topicPriorityReason(planningTopic.reason())
                .resourceReason(null)
                .build());

        for (TopicPrereq prereq : buildRequiredPrereqMap().getOrDefault(step.getTopic().getId(), List.of())) {
            PrereqStatus status = resolvePrereqStatus(prereq.getPrereqTopic().getId(), step.getTopic().getId(), knownTopicIds, topicOrder);
            planStepExplanationPrereqRepository.save(PlanStepExplanationPrereq.builder()
                    .id(new PlanStepExplanationPrereqId(step.getId(), prereq.getPrereqTopic().getId()))
                    .explanation(explanation)
                    .prereqTopic(prereq.getPrereqTopic())
                    .prereqStatus(status)
                    .build());
        }
    }

    private PrereqStatus resolvePrereqStatus(Long prereqTopicId,
                                             Long currentTopicId,
                                             Set<Long> knownTopicIds,
                                             Map<Long, Integer> topicOrder) {
        if (knownTopicIds.contains(prereqTopicId)) {
            return PrereqStatus.DONE;
        }

        Integer prereqPosition = topicOrder.get(prereqTopicId);
        Integer currentPosition = topicOrder.get(currentTopicId);
        if (prereqPosition != null && currentPosition != null && prereqPosition < currentPosition) {
            return PrereqStatus.IN_PREVIOUS_STEPS;
        }

        return PrereqStatus.MISSING;
    }

    private PlanShortResponse toShortResponse(Plan plan) {
        return PlanShortResponse.builder()
                .id(plan.getId())
                .roleId(plan.getRole().getId())
                .status(plan.getStatus())
                .scenarioType(plan.getScenarioType())
                .scenarioLabel(plan.getScenarioLabel())
                .basePlanId(plan.getBasePlan() != null ? plan.getBasePlan().getId() : null)
                .createdAt(plan.getCreatedAt())
                .build();
    }

    private PlanWeekResponse toWeekResponse(PlanWeek week) {
        List<PlanStepResponse> stepResponses = planStepRepository.findAllByPlanWeek_IdOrderByOrderInWeekAsc(week.getId())
                .stream()
                .map(this::toStepResponse)
                .toList();

        return PlanWeekResponse.builder()
                .id(week.getId())
                .weekIndex(week.getWeekIndex())
                .hoursBudget(week.getHoursBudget())
                .hoursPlanned(week.getHoursPlanned())
                .steps(stepResponses)
                .build();
    }

    private PlanStepResponse toStepResponse(PlanStep step) {
        List<PlanStepResourceResponse> resources = planStepResourceRepository.findAllByPlanStep_Id(step.getId()).stream()
                .map(resource -> PlanStepResourceResponse.builder()
                        .resourceId(resource.getResource().getId())
                        .isPrimary(resource.isPrimary())
                        .build())
                .toList();

        PlanStepExplanationResponse explanation = planStepExplanationRepository.findById(step.getId())
                .map(this::toExplanationResponse)
                .orElse(null);

        return PlanStepResponse.builder()
                .id(step.getId())
                .topicId(step.getTopic().getId())
                .orderInWeek(step.getOrderInWeek())
                .plannedHours(step.getPlannedHours())
                .isOptional(step.isOptional())
                .resources(resources)
                .explanation(explanation)
                .build();
    }

    private PlanStepExplanationResponse toExplanationResponse(PlanStepExplanation explanation) {
        List<PlanStepExplanationResponse.PrereqItem> prereqs =
                planStepExplanationPrereqRepository.findAllByExplanation_PlanStepId(explanation.getPlanStepId())
                        .stream()
                        .map(item -> new PlanStepExplanationResponse.PrereqItem(
                                item.getPrereqTopic().getId(),
                                item.getPrereqStatus()
                        ))
                        .toList();

        return PlanStepExplanationResponse.builder()
                .ruleApplied(explanation.getRuleApplied())
                .topicPriorityReason(explanation.getTopicPriorityReason())
                .resourceReason(explanation.getResourceReason())
                .prereqs(prereqs)
                .build();
    }

    private PlanParamsSnapshotResponse toSnapshotResponse(PlanParamsSnapshot snapshot) {
        return PlanParamsSnapshotResponse.builder()
                .hoursPerWeek(snapshot.getHoursPerWeek())
                .prefsLanguage(snapshot.getPrefsLanguage())
                .prefsResourceTypes(snapshot.getPrefsResourceTypes())
                .algoVersion(snapshot.getAlgoVersion())
                .build();
    }

    private Map<Long, List<TopicPrereq>> buildRequiredPrereqMap() {
        return topicPrereqRepository.findAll().stream()
                .filter(prereq -> prereq.getRelationType() == PrereqRelationType.REQUIRED)
                .collect(Collectors.groupingBy(prereq -> prereq.getNextTopic().getId()));
    }

    private BigDecimal defaultHours(Topic topic) {
        return topic.getEstimatedHours() != null && topic.getEstimatedHours().signum() > 0
                ? topic.getEstimatedHours()
                : BigDecimal.ONE;
    }

    private int rolePriority(RoleTopic roleTopic) {
        return roleTopic != null && roleTopic.getPriority() != null
                ? roleTopic.getPriority()
                : Integer.MAX_VALUE;
    }

    private RoleGoal singleOrNull(List<RoleGoal> candidates, String ambiguousMessage) {
        if (candidates.size() > 1) {
            throw new AiRouteValidationException(ambiguousMessage);
        }
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    private String normalize(String value) {
        return StringUtils.hasText(value) ? value.trim().toLowerCase(Locale.ROOT) : "";
    }

    private record PlanningTopic(
            Topic topic,
            BigDecimal plannedHours,
            Integer aiPriority,
            Integer aiOrder,
            String reason,
            boolean optional,
            boolean aiSuggested
    ) {
    }
}
