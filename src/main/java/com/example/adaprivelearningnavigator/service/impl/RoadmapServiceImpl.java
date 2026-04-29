package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Resource;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicResource;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.Quiz;
import com.example.adaprivelearningnavigator.repo.QuizRepository;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicPrereqRepository;
import com.example.adaprivelearningnavigator.repo.TopicResourceRepository;
import com.example.adaprivelearningnavigator.service.RoadmapService;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapDetailResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapQuizSummaryResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapResourceResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapSummaryResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapTopicDetailResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapTopicResponse;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import com.example.adaprivelearningnavigator.service.support.KnowledgeBaseLocalizationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoadmapServiceImpl implements RoadmapService {

    private static final Pattern ROADMAP_SOURCE_PATTERN =
            Pattern.compile("(?i)\\s*из каталога\\s+roadmap\\.sh\\.?\\s*");
    private static final Pattern ADAPTATION_NOTE_PATTERN =
            Pattern.compile("(?i)\\s*темы и материалы адаптированы для русскоязычного интерфейса сервиса\\.?\\s*");
    private static final Pattern ADDED_FROM_SOURCE_PATTERN =
            Pattern.compile("(?i)\\s*добавлено из\\s+roadmap\\.sh\\.?\\s*");
    private static final Pattern MULTISPACE_PATTERN =
            Pattern.compile("\\s{2,}");

    private final RoleGoalRepository roleGoalRepository;
    private final RoleTopicRepository roleTopicRepository;
    private final TopicPrereqRepository topicPrereqRepository;
    private final TopicResourceRepository topicResourceRepository;
    private final QuizRepository quizRepository;

    public RoadmapServiceImpl(RoleGoalRepository roleGoalRepository,
                              RoleTopicRepository roleTopicRepository,
                              TopicPrereqRepository topicPrereqRepository,
                              TopicResourceRepository topicResourceRepository,
                              QuizRepository quizRepository) {
        this.roleGoalRepository = roleGoalRepository;
        this.roleTopicRepository = roleTopicRepository;
        this.topicPrereqRepository = topicPrereqRepository;
        this.topicResourceRepository = topicResourceRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public PageResponse<RoadmapSummaryResponse> getRoadmaps(int page, int size) {
        List<RoleGoal> allRoadmaps = loadActiveRoadmaps();
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        int fromIndex = Math.min(safePage * safeSize, allRoadmaps.size());
        int toIndex = Math.min(fromIndex + safeSize, allRoadmaps.size());

        List<RoadmapSummaryResponse> items = allRoadmaps.subList(fromIndex, toIndex).stream()
                .map(this::toSummaryResponse)
                .toList();

        return PageResponse.<RoadmapSummaryResponse>builder()
                .items(items)
                .total(allRoadmaps.size())
                .page(safePage)
                .size(safeSize)
                .build();
    }

    @Override
    public RoadmapDetailResponse getRoadmap(Long roleId) {
        RoleGoal roadmap = findRoadmap(roleId);
        Map<Long, RoleTopic> roleTopicMeta = roleTopicsByTopicId(roleId);
        Map<Long, List<TopicPrereq>> requiredPrereqMap = requiredPrereqMap();
        Map<Long, Topic> roleScope = buildRoleScope(roleTopicMeta.values(), requiredPrereqMap);
        List<Topic> orderedTopics = orderTopics(roleScope.values(), roleTopicMeta, requiredPrereqMap);

        List<RoadmapTopicResponse> topicResponses = orderedTopics.stream()
                .map(topic -> toRoadmapTopicResponse(topic, roleTopicMeta.get(topic.getId()), requiredPrereqMap))
                .toList();
        String localizedRoleName = KnowledgeBaseLocalizationUtil.localizeRoleName(roadmap.getCode(), roadmap.getName());

        return RoadmapDetailResponse.builder()
                .id(roadmap.getId())
                .code(roadmap.getCode())
                .name(localizedRoleName)
                .category(KnowledgeBaseLocalizationUtil.roadmapCategory(roadmap.getCode()))
                .categoryLabel(KnowledgeBaseLocalizationUtil.roadmapCategoryLabel(roadmap.getCode()))
                .description(sanitizeDescription(roadmap.getDescription(), roadmap.getName(), localizedRoleName, null, null))
                .topicCount(topicResponses.size())
                .requiredTopicCount((int) roleTopicMeta.values().stream().filter(RoleTopic::isRequired).count())
                .totalEstimatedHours(sumEstimatedHours(orderedTopics))
                .topics(topicResponses)
                .build();
    }

    @Override
    public RoadmapTopicDetailResponse getRoadmapTopic(Long roleId, Long topicId) {
        RoleGoal roadmap = findRoadmap(roleId);
        Map<Long, RoleTopic> roleTopicMeta = roleTopicsByTopicId(roleId);
        Map<Long, List<TopicPrereq>> requiredPrereqMap = requiredPrereqMap();
        Map<Long, Topic> roleScope = buildRoleScope(roleTopicMeta.values(), requiredPrereqMap);

        Topic topic = roleScope.get(topicId);
        if (topic == null) {
            throw new NotFoundException("Тема не найдена в выбранном roadmap");
        }

        Map<Long, List<TopicPrereq>> unlocksMap = topicPrereqRepository.findAll().stream()
                .filter(prereq -> prereq.getRelationType() == PrereqRelationType.REQUIRED)
                .filter(prereq -> roleScope.containsKey(prereq.getNextTopic().getId()))
                .collect(Collectors.groupingBy(prereq -> prereq.getPrereqTopic().getId()));

        List<RoadmapTopicResponse> prereqs = requiredPrereqMap.getOrDefault(topicId, List.of()).stream()
                .map(TopicPrereq::getPrereqTopic)
                .distinct()
                .sorted(Comparator.comparing(Topic::getTitle, String.CASE_INSENSITIVE_ORDER))
                .map(prereqTopic -> toRoadmapTopicResponse(prereqTopic, roleTopicMeta.get(prereqTopic.getId()), requiredPrereqMap))
                .toList();

        List<RoadmapTopicResponse> unlocks = unlocksMap.getOrDefault(topicId, List.of()).stream()
                .map(TopicPrereq::getNextTopic)
                .distinct()
                .sorted(Comparator.comparing(Topic::getTitle, String.CASE_INSENSITIVE_ORDER))
                .map(nextTopic -> toRoadmapTopicResponse(nextTopic, roleTopicMeta.get(nextTopic.getId()), requiredPrereqMap))
                .toList();

        List<RoadmapResourceResponse> resources = topicResourceRepository.findAllByTopic_IdOrderByRankAsc(topicId).stream()
                .map(this::toRoadmapResourceResponse)
                .toList();

        Quiz quiz = quizRepository.findByTopic_Id(topicId).orElse(null);
        String localizedRoleName = KnowledgeBaseLocalizationUtil.localizeRoleName(roadmap.getCode(), roadmap.getName());
        String localizedTopicTitle = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());

        return RoadmapTopicDetailResponse.builder()
                .roleId(roadmap.getId())
                .roleCode(roadmap.getCode())
                .roleName(localizedRoleName)
                .topicId(topic.getId())
                .topicCode(topic.getCode())
                .topicTitle(localizedTopicTitle)
                .description(sanitizeDescription(topic.getDescription(), roadmap.getName(), localizedRoleName, topic.getTitle(), localizedTopicTitle))
                .level(topic.getLevel())
                .isCore(topic.isCore())
                .estimatedHours(topic.getEstimatedHours())
                .priority(rolePriority(roleTopicMeta.get(topic.getId())))
                .isRequired(isRequired(roleTopicMeta.get(topic.getId())))
                .prereqs(prereqs)
                .unlocks(unlocks)
                .resources(resources)
                .quiz(RoadmapQuizSummaryResponse.builder()
                        .id(quiz != null ? quiz.getId() : null)
                        .title(quiz != null ? quiz.getTitle() : null)
                        .available(quiz != null)
                        .build())
                .build();
    }

    private List<RoleGoal> loadActiveRoadmaps() {
        List<RoleGoal> active = roleGoalRepository.findAllByStatusOrderByNameAsc(EntityStatus.ACTIVE);
        if (!active.isEmpty()) {
            return visibleMvpRoadmaps(active);
        }
        return visibleMvpRoadmaps(roleGoalRepository.findAll()).stream()
                .sorted(Comparator.comparing(RoleGoal::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private List<RoleGoal> visibleMvpRoadmaps(Collection<RoleGoal> roadmaps) {
        return roadmaps.stream()
                .filter(roleGoal -> KnowledgeBaseLocalizationUtil.isMvpRoadmap(roleGoal.getCode()))
                .toList();
    }

    private RoleGoal findRoadmap(Long roleId) {
        return roleGoalRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Roadmap не найден"));
    }

    private RoadmapSummaryResponse toSummaryResponse(RoleGoal roleGoal) {
        List<RoleTopic> roleTopics = roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(roleGoal.getId());
        BigDecimal totalEstimatedHours = sumEstimatedHours(roleTopics.stream()
                .map(RoleTopic::getTopic)
                .toList());
        String localizedRoleName = KnowledgeBaseLocalizationUtil.localizeRoleName(roleGoal.getCode(), roleGoal.getName());

        return RoadmapSummaryResponse.builder()
                .id(roleGoal.getId())
                .code(roleGoal.getCode())
                .name(localizedRoleName)
                .category(KnowledgeBaseLocalizationUtil.roadmapCategory(roleGoal.getCode()))
                .categoryLabel(KnowledgeBaseLocalizationUtil.roadmapCategoryLabel(roleGoal.getCode()))
                .description(sanitizeDescription(roleGoal.getDescription(), roleGoal.getName(), localizedRoleName, null, null))
                .topicCount(roleTopics.size())
                .requiredTopicCount((int) roleTopics.stream().filter(RoleTopic::isRequired).count())
                .totalEstimatedHours(totalEstimatedHours)
                .build();
    }

    private Map<Long, RoleTopic> roleTopicsByTopicId(Long roleId) {
        return roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(roleId).stream()
                .collect(Collectors.toMap(
                        roleTopic -> roleTopic.getTopic().getId(),
                        Function.identity(),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    private Map<Long, List<TopicPrereq>> requiredPrereqMap() {
        return topicPrereqRepository.findAll().stream()
                .filter(prereq -> prereq.getRelationType() == PrereqRelationType.REQUIRED)
                .collect(Collectors.groupingBy(prereq -> prereq.getNextTopic().getId()));
    }

    private Map<Long, Topic> buildRoleScope(Collection<RoleTopic> roleTopics,
                                            Map<Long, List<TopicPrereq>> requiredPrereqMap) {
        Map<Long, Topic> result = roleTopics.stream()
                .map(RoleTopic::getTopic)
                .collect(Collectors.toMap(Topic::getId, Function.identity(), (left, right) -> left, LinkedHashMap::new));

        Deque<Topic> stack = new ArrayDeque<>(result.values());
        while (!stack.isEmpty()) {
            Topic current = stack.pop();
            for (TopicPrereq prereq : requiredPrereqMap.getOrDefault(current.getId(), List.of())) {
                Topic prereqTopic = prereq.getPrereqTopic();
                if (!result.containsKey(prereqTopic.getId())) {
                    result.put(prereqTopic.getId(), prereqTopic);
                    stack.push(prereqTopic);
                }
            }
        }

        return result;
    }

    private List<Topic> orderTopics(Collection<Topic> topics,
                                    Map<Long, RoleTopic> roleTopicMeta,
                                    Map<Long, List<TopicPrereq>> requiredPrereqMap) {
        Map<Long, Topic> topicById = topics.stream()
                .collect(Collectors.toMap(Topic::getId, Function.identity()));
        Map<Long, List<Long>> outgoing = new HashMap<>();
        Map<Long, Integer> indegree = new HashMap<>();

        for (Topic topic : topics) {
            indegree.put(topic.getId(), 0);
            outgoing.put(topic.getId(), new ArrayList<>());
        }

        for (Topic topic : topics) {
            for (TopicPrereq prereq : requiredPrereqMap.getOrDefault(topic.getId(), List.of())) {
                Long prereqId = prereq.getPrereqTopic().getId();
                if (!topicById.containsKey(prereqId)) {
                    continue;
                }
                outgoing.get(prereqId).add(topic.getId());
                indegree.computeIfPresent(topic.getId(), (key, value) -> value + 1);
            }
        }

        Comparator<Topic> comparator = Comparator
                .comparing((Topic topic) -> rolePriority(roleTopicMeta.get(topic.getId())))
                .thenComparing(topic -> isRequired(roleTopicMeta.get(topic.getId())) ? 0 : 1)
                .thenComparing(topic -> topic.isCore() ? 0 : 1)
                .thenComparing(topic -> topic.getEstimatedHours() == null ? BigDecimal.ZERO : topic.getEstimatedHours())
                .thenComparing(Topic::getTitle, String.CASE_INSENSITIVE_ORDER);

        Deque<Topic> queue = topics.stream()
                .filter(topic -> indegree.getOrDefault(topic.getId(), 0) == 0)
                .sorted(comparator)
                .collect(Collectors.toCollection(ArrayDeque::new));

        List<Topic> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            Topic current = queue.removeFirst();
            ordered.add(current);

            List<Topic> unlocked = outgoing.getOrDefault(current.getId(), List.of()).stream()
                    .map(topicById::get)
                    .toList();
            for (Topic next : unlocked) {
                int newIndegree = indegree.computeIfPresent(next.getId(), (key, value) -> value - 1);
                if (newIndegree == 0) {
                    queue.add(next);
                }
            }

            if (queue.size() > 1) {
                List<Topic> sorted = queue.stream().sorted(comparator).toList();
                queue.clear();
                queue.addAll(sorted);
            }
        }

        if (ordered.size() == topics.size()) {
            return ordered;
        }

        return topics.stream()
                .sorted(comparator)
                .toList();
    }

    private RoadmapTopicResponse toRoadmapTopicResponse(Topic topic,
                                                        RoleTopic roleTopic,
                                                        Map<Long, List<TopicPrereq>> requiredPrereqMap) {
        List<TopicPrereq> prereqs = requiredPrereqMap.getOrDefault(topic.getId(), List.of());
        String localizedTopicTitle = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());

        return RoadmapTopicResponse.builder()
                .topicId(topic.getId())
                .topicCode(topic.getCode())
                .topicTitle(localizedTopicTitle)
                .description(sanitizeDescription(topic.getDescription(), null, null, topic.getTitle(), localizedTopicTitle))
                .level(topic.getLevel())
                .isCore(topic.isCore())
                .estimatedHours(topic.getEstimatedHours())
                .priority(rolePriority(roleTopic))
                .isRequired(isRequired(roleTopic))
                .prereqTopicIds(prereqs.stream().map(item -> item.getPrereqTopic().getId()).distinct().toList())
                .prereqTopicCodes(prereqs.stream().map(item -> item.getPrereqTopic().getCode()).distinct().toList())
                .build();
    }

    private RoadmapResourceResponse toRoadmapResourceResponse(TopicResource topicResource) {
        Resource resource = topicResource.getResource();
        return RoadmapResourceResponse.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .url(resource.getUrl())
                .type(resource.getType())
                .language(resource.getLanguage())
                .durationMinutes(resource.getDurationMinutes())
                .provider(sanitizeProvider(resource.getProvider()))
                .difficulty(resource.getDifficulty())
                .rank(topicResource.getRank())
                .build();
    }

    private BigDecimal sumEstimatedHours(Collection<Topic> topics) {
        return topics.stream()
                .map(Topic::getEstimatedHours)
                .filter(hours -> hours != null && hours.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int rolePriority(RoleTopic roleTopic) {
        return roleTopic != null && roleTopic.getPriority() != null
                ? roleTopic.getPriority()
                : Integer.MAX_VALUE;
    }

    private boolean isRequired(RoleTopic roleTopic) {
        return roleTopic != null && roleTopic.isRequired();
    }

    private String sanitizeDescription(String description) {
        return sanitizeDescription(description, null, null, null, null);
    }

    private String sanitizeDescription(String description,
                                       String originalRoleName,
                                       String localizedRoleName,
                                       String originalTopicTitle,
                                       String localizedTopicTitle) {
        return KnowledgeBaseLocalizationUtil.localizeDescription(
                description,
                originalRoleName,
                localizedRoleName,
                originalTopicTitle,
                localizedTopicTitle
        );
    }

    private String sanitizeProvider(String provider) {
        return KnowledgeBaseLocalizationUtil.hideSourceProvider(provider);
    }

/*
        if (description == null || description.isBlank()) {
            return description;
        }

        String sanitized = description
                .replace("из каталога roadmap.sh.", "")
                .replace("Из каталога roadmap.sh.", "")
                .replace("из каталога roadmap.sh", "")
                .replace("Из каталога roadmap.sh", "")
                .replace("Темы и материалы адаптированы для русскоязычного интерфейса сервиса.", "")
                .replace("Темы и материалы адаптированы для русскоязычного интерфейса сервиса", "")
                .replace("темы и материалы адаптированы для русскоязычного интерфейса сервиса.", "")
                .replace("темы и материалы адаптированы для русскоязычного интерфейса сервиса", "")
                .replace("Добавлено из roadmap.sh.", "")
                .replace("Добавлено из roadmap.sh", "")
                .replace("добавлено из roadmap.sh.", "")
                .replace("добавлено из roadmap.sh", "");

        sanitized = MULTISPACE_PATTERN.matcher(sanitized.trim()).replaceAll(" ");
        sanitized = sanitized.replace(" .", ".").replace(" :", ":").trim();

        if (sanitized.endsWith(":")) {
            sanitized = sanitized.substring(0, sanitized.length() - 1).trim();
        }

        return sanitized;
    }

    private String sanitizeProvider(String provider) {
        if (provider == null || provider.isBlank()) {
            return provider;
        }

        return "roadmap.sh".equalsIgnoreCase(provider.trim())
                ? null
                : provider;
    }
*/
}
