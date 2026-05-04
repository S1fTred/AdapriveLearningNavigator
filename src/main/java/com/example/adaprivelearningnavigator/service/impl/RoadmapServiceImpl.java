package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    private static final int MIN_RUSSIAN_TOPIC_RESOURCES = 5;

    private final RoleGoalRepository roleGoalRepository;
    private final RoleTopicRepository roleTopicRepository;
    private final TopicPrereqRepository topicPrereqRepository;
    private final TopicResourceRepository topicResourceRepository;
    private final QuizRepository quizRepository;

    private record FallbackResourceTemplate(
            String title,
            String url,
            ResourceType type,
            String language,
            String provider
    ) {
    }

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

        Quiz quiz = quizRepository.findByTopic_Id(topicId).orElse(null);
        String localizedRoleName = KnowledgeBaseLocalizationUtil.localizeRoleName(roadmap.getCode(), roadmap.getName());
        String localizedTopicTitle = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());
        List<RoadmapResourceResponse> resources = topicResourceRepository.findAllByTopic_IdOrderByRankAsc(topicId).stream()
                .map(this::toRoadmapResourceResponse)
                .toList();
        resources = ensureMinimumRussianResources(resources, topic, localizedTopicTitle);

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

    private List<RoadmapResourceResponse> ensureMinimumRussianResources(List<RoadmapResourceResponse> resources,
                                                                        Topic topic,
                                                                        String localizedTopicTitle) {
        long russianResourceCount = resources.stream()
                .filter(this::isRussianResource)
                .count();
        if (russianResourceCount >= MIN_RUSSIAN_TOPIC_RESOURCES) {
            return resources;
        }

        List<RoadmapResourceResponse> result = new ArrayList<>(resources);
        Set<String> urls = result.stream()
                .map(RoadmapResourceResponse::url)
                .filter(url -> url != null && !url.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (RoadmapResourceResponse fallbackResource : fallbackResources(topic, localizedTopicTitle)) {
            String url = fallbackResource.url();
            if (url != null && urls.add(url)) {
                result.add(fallbackResource);
                if (isRussianResource(fallbackResource)) {
                    russianResourceCount++;
                }
            }
            if (russianResourceCount >= MIN_RUSSIAN_TOPIC_RESOURCES) {
                break;
            }
        }

        return result;
    }

    private boolean isRussianResource(RoadmapResourceResponse resource) {
        if (resource.language() != null && "ru".equalsIgnoreCase(resource.language())) {
            return true;
        }
        String provider = resource.provider() != null ? resource.provider().toLowerCase() : "";
        String url = resource.url() != null ? resource.url().toLowerCase() : "";
        return containsAny(provider,
                "habr", "tproger", "proglib", "stepik", "metanit", "html academy", "hexlet",
                "javarush", "swiftbook", "postgres pro", "sql-ex", "яндекс", "losst", "итмо"
        ) || containsAny(url,
                "/ru/", "habr.com/ru", "tproger.ru", "proglib.io", "stepik.org", "metanit.com",
                "learn.javascript.ru", "ru.react.dev", "ru.vuejs.org", "postgrespro.ru", "docs-python.ru",
                "sql-ex.ru", "git-scm.com/book/ru", "developer.mozilla.org/ru", "learn.microsoft.com/ru-ru",
                "kotlinlang.ru", "doc.rust-lang.ru", "gobyexample.com.ru", "swiftbook.org", "losst.pro"
        );
    }

    private List<RoadmapResourceResponse> fallbackResources(Topic topic, String localizedTopicTitle) {
        String query = localizedTopicTitle + " " + topic.getTitle();
        String fingerprint = normalizeResourceFingerprint(topic, localizedTopicTitle);
        List<FallbackResourceTemplate> templates = curatedFallbackResources(fingerprint);
        List<FallbackResourceTemplate> result = new ArrayList<>(templates);

        result.addAll(searchFallbackResources(query, localizedTopicTitle));

        String difficulty = topic.getLevel() != null ? topic.getLevel().name() : null;
        List<RoadmapResourceResponse> resources = new ArrayList<>();
        int rank = 90;
        Set<String> urls = new LinkedHashSet<>();
        for (FallbackResourceTemplate template : result) {
            if (!urls.add(template.url())) {
                continue;
            }
            resources.add(RoadmapResourceResponse.builder()
                    .id(null)
                    .title(template.title())
                    .url(template.url())
                    .type(template.type())
                    .language(template.language())
                    .provider(template.provider())
                    .difficulty(difficulty)
                    .rank(rank++)
                    .build());
        }

        return resources;
    }

    private List<FallbackResourceTemplate> curatedFallbackResources(String fingerprint) {
        if (containsAny(fingerprint, "sql", "postgres", "database", "баз данн", "запрос")) {
            return List.of(
                    resource("SQL: интерактивные упражнения", "https://sql-ex.ru/", ResourceType.INTERACTIVE, "ru", "SQL-EX"),
                    resource("Документация PostgreSQL на русском", "https://postgrespro.ru/docs/postgresql/current/", ResourceType.ARTICLE, "ru", "Postgres Pro"),
                    resource("SQL и реляционные базы данных", "https://metanit.com/sql/", ResourceType.COURSE, "ru", "Metanit")
            );
        }
        if (containsAny(fingerprint, "java", "jvm", "maven", "gradle")) {
            return List.of(
                    resource("Java: руководство Metanit", "https://metanit.com/java/tutorial/", ResourceType.COURSE, "ru", "Metanit"),
                    resource("JavaRush: лекции по Java", "https://javarush.com/quests/lectures", ResourceType.COURSE, "ru", "JavaRush"),
                    resource("Официальная документация Java", "https://docs.oracle.com/en/java/", ResourceType.ARTICLE, "en", "Oracle")
            );
        }
        if (containsAny(fingerprint, "python", "django", "pandas", "numpy")) {
            return List.of(
                    resource("Python: документация и справочник на русском", "https://docs-python.ru/", ResourceType.ARTICLE, "ru", "docs-python.ru"),
                    resource("Python: руководство Metanit", "https://metanit.com/python/tutorial/", ResourceType.COURSE, "ru", "Metanit"),
                    resource("Официальная документация Python", "https://docs.python.org/3/", ResourceType.ARTICLE, "en", "Python Docs")
            );
        }
        if (containsAny(fingerprint, "go ", "golang")) {
            return List.of(
                    resource("Go by Example на русском", "https://gobyexample.com.ru/", ResourceType.COURSE, "ru", "Go by Example"),
                    resource("A Tour of Go", "https://go.dev/tour/", ResourceType.INTERACTIVE, "en", "Go"),
                    resource("Go Documentation", "https://go.dev/doc/", ResourceType.ARTICLE, "en", "Go")
            );
        }
        if (containsAny(fingerprint, "rust")) {
            return List.of(
                    resource("The Rust Programming Language на русском", "https://doc.rust-lang.ru/book/", ResourceType.BOOK, "ru", "Rust"),
                    resource("Rust by Example", "https://doc.rust-lang.org/rust-by-example/", ResourceType.COURSE, "en", "Rust"),
                    resource("Rust Documentation", "https://www.rust-lang.org/learn", ResourceType.ARTICLE, "en", "Rust")
            );
        }
        if (containsAny(fingerprint, "kotlin")) {
            return List.of(
                    resource("Kotlin Documentation", "https://kotlinlang.org/docs/home.html", ResourceType.ARTICLE, "en", "Kotlin"),
                    resource("Kotlin Koans", "https://play.kotlinlang.org/koans/overview", ResourceType.INTERACTIVE, "en", "Kotlin"),
                    resource("Android Kotlin на русском", "https://developer.android.com/kotlin?hl=ru", ResourceType.ARTICLE, "ru", "Android")
            );
        }
        if (containsAny(fingerprint, "php", "laravel")) {
            return List.of(
                    resource("PHP: руководство на русском", "https://www.php.net/manual/ru/", ResourceType.ARTICLE, "ru", "PHP"),
                    resource("Laravel Documentation", "https://laravel.com/docs", ResourceType.ARTICLE, "en", "Laravel"),
                    resource("PHP: руководство Metanit", "https://metanit.com/php/tutorial/", ResourceType.COURSE, "ru", "Metanit")
            );
        }
        if (containsAny(fingerprint, "ruby", "rails")) {
            return List.of(
                    resource("Ruby: документация на русском", "https://www.ruby-lang.org/ru/documentation/", ResourceType.ARTICLE, "ru", "Ruby"),
                    resource("Ruby on Rails Guides", "https://guides.rubyonrails.org/", ResourceType.COURSE, "en", "Ruby on Rails"),
                    resource("Ruby: материалы на Habr", "https://habr.com/ru/search/?q=Ruby&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "c++", "cpp")) {
            return List.of(
                    resource("C++: руководство Metanit", "https://metanit.com/cpp/tutorial/", ResourceType.COURSE, "ru", "Metanit"),
                    resource("cppreference", "https://en.cppreference.com/w/", ResourceType.ARTICLE, "en", "cppreference"),
                    resource("C++ на Habr", "https://habr.com/ru/search/?q=C%2B%2B&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "c#", "csharp", ".net", "asp.net")) {
            return List.of(
                    resource("C# и .NET: руководство Metanit", "https://metanit.com/sharp/tutorial/", ResourceType.COURSE, "ru", "Metanit"),
                    resource("Документация .NET", "https://learn.microsoft.com/ru-ru/dotnet/", ResourceType.ARTICLE, "ru", "Microsoft Learn"),
                    resource("ASP.NET Core Documentation", "https://learn.microsoft.com/ru-ru/aspnet/core/", ResourceType.ARTICLE, "ru", "Microsoft Learn")
            );
        }
        if (containsAny(fingerprint, "javascript", "typescript", "node", "promise", "async")) {
            return List.of(
                    resource("Современный учебник JavaScript", "https://learn.javascript.ru/", ResourceType.COURSE, "ru", "learn.javascript.ru"),
                    resource("JavaScript на MDN", "https://developer.mozilla.org/ru/docs/Web/JavaScript", ResourceType.ARTICLE, "ru", "MDN"),
                    resource("TypeScript Handbook", "https://www.typescriptlang.org/docs/", ResourceType.ARTICLE, "en", "TypeScript")
            );
        }
        if (containsAny(fingerprint, "html", "semantic", "accessibility", "a11y")) {
            return List.of(
                    resource("HTML на MDN", "https://developer.mozilla.org/ru/docs/Web/HTML", ResourceType.ARTICLE, "ru", "MDN"),
                    resource("HTML Academy: основы HTML", "https://htmlacademy.ru/courses/basic-html-css", ResourceType.INTERACTIVE, "ru", "HTML Academy"),
                    resource("Доступность в вебе на MDN", "https://developer.mozilla.org/ru/docs/Web/Accessibility", ResourceType.ARTICLE, "ru", "MDN")
            );
        }
        if (containsAny(fingerprint, "css", "flexbox", "grid", "layout")) {
            return List.of(
                    resource("CSS на MDN", "https://developer.mozilla.org/ru/docs/Web/CSS", ResourceType.ARTICLE, "ru", "MDN"),
                    resource("CSS Grid Layout на MDN", "https://developer.mozilla.org/ru/docs/Web/CSS/CSS_grid_layout", ResourceType.ARTICLE, "ru", "MDN"),
                    resource("Flexbox на MDN", "https://developer.mozilla.org/ru/docs/Learn/CSS/CSS_layout/Flexbox", ResourceType.ARTICLE, "ru", "MDN")
            );
        }
        if (containsAny(fingerprint, "react", "next.js", "nextjs")) {
            return List.of(
                    resource("React: обучение", "https://ru.react.dev/learn", ResourceType.COURSE, "ru", "React"),
                    resource("React: справочник API", "https://ru.react.dev/reference/react", ResourceType.ARTICLE, "ru", "React"),
                    resource("Next.js Documentation", "https://nextjs.org/docs", ResourceType.ARTICLE, "en", "Next.js")
            );
        }
        if (containsAny(fingerprint, "vue")) {
            return List.of(
                    resource("Vue: руководство", "https://ru.vuejs.org/guide/introduction.html", ResourceType.COURSE, "ru", "Vue"),
                    resource("Vue: примеры", "https://ru.vuejs.org/examples/", ResourceType.INTERACTIVE, "ru", "Vue"),
                    resource("Vue API", "https://ru.vuejs.org/api/", ResourceType.ARTICLE, "ru", "Vue")
            );
        }
        if (containsAny(fingerprint, "angular")) {
            return List.of(
                    resource("Angular Documentation", "https://angular.dev/overview", ResourceType.ARTICLE, "en", "Angular"),
                    resource("Angular Tutorial", "https://angular.dev/tutorials", ResourceType.COURSE, "en", "Angular"),
                    resource("Angular на Habr", "https://habr.com/ru/search/?q=Angular&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "git", "github", "version control")) {
            return List.of(
                    resource("Pro Git на русском", "https://git-scm.com/book/ru/v2", ResourceType.BOOK, "ru", "Git"),
                    resource("Git: справочник команд", "https://git-scm.com/docs", ResourceType.ARTICLE, "en", "Git"),
                    resource("GitHub Docs", "https://docs.github.com/ru", ResourceType.ARTICLE, "ru", "GitHub")
            );
        }
        if (containsAny(fingerprint, "linux", "bash", "shell", "cli", "command line")) {
            return List.of(
                    resource("Команды Linux: справочник", "https://losst.pro/komandy-linux", ResourceType.ARTICLE, "ru", "Losst"),
                    resource("Bash Reference Manual", "https://www.gnu.org/software/bash/manual/bash.html", ResourceType.ARTICLE, "en", "GNU"),
                    resource("Linux на Habr", "https://habr.com/ru/search/?q=Linux%20Bash&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "http", "rest", "api", "cors", "graphql")) {
            return List.of(
                    resource("HTTP на MDN", "https://developer.mozilla.org/ru/docs/Web/HTTP", ResourceType.ARTICLE, "ru", "MDN"),
                    resource("REST API: материалы на Habr", "https://habr.com/ru/search/?q=REST%20API&target_type=posts", ResourceType.ARTICLE, "ru", "Habr"),
                    resource("GraphQL Learn", "https://graphql.org/learn/", ResourceType.COURSE, "en", "GraphQL")
            );
        }
        if (containsAny(fingerprint, "spring", "hibernate", "jpa", "orm")) {
            return List.of(
                    resource("Spring Framework и Spring Boot", "https://metanit.com/java/spring/", ResourceType.COURSE, "ru", "Metanit"),
                    resource("Spring Guides", "https://spring.io/guides", ResourceType.COURSE, "en", "Spring"),
                    resource("Hibernate ORM Documentation", "https://hibernate.org/orm/documentation/", ResourceType.ARTICLE, "en", "Hibernate")
            );
        }
        if (containsAny(fingerprint, "docker", "container")) {
            return List.of(
                    resource("Docker Documentation", "https://docs.docker.com/", ResourceType.ARTICLE, "en", "Docker"),
                    resource("Docker: материалы на Habr", "https://habr.com/ru/search/?q=Docker&target_type=posts", ResourceType.ARTICLE, "ru", "Habr"),
                    resource("Docker: getting started", "https://docs.docker.com/get-started/", ResourceType.COURSE, "en", "Docker")
            );
        }
        if (containsAny(fingerprint, "kubernetes", "k8s")) {
            return List.of(
                    resource("Kubernetes: документация на русском", "https://kubernetes.io/ru/docs/home/", ResourceType.ARTICLE, "ru", "Kubernetes"),
                    resource("Kubernetes: основы", "https://kubernetes.io/ru/docs/tutorials/kubernetes-basics/", ResourceType.COURSE, "ru", "Kubernetes"),
                    resource("Kubernetes на Habr", "https://habr.com/ru/search/?q=Kubernetes&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "security", "jwt", "oauth", "owasp", "xss", "csrf")) {
            return List.of(
                    resource("OWASP Cheat Sheet Series", "https://cheatsheetseries.owasp.org/", ResourceType.ARTICLE, "en", "OWASP"),
                    resource("Безопасность в вебе на MDN", "https://developer.mozilla.org/ru/docs/Web/Security", ResourceType.ARTICLE, "ru", "MDN"),
                    resource("OAuth 2.0: материалы на Habr", "https://habr.com/ru/search/?q=OAuth%202.0&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "algorithm", "data structure", "алгоритм", "структур")) {
            return List.of(
                    resource("Алгоритмы и структуры данных", "https://e-maxx.ru/algo/", ResourceType.ARTICLE, "ru", "E-maxx"),
                    resource("Вики-конспекты ИТМО", "https://neerc.ifmo.ru/wiki/", ResourceType.ARTICLE, "ru", "ИТМО"),
                    resource("CP-Algorithms", "https://cp-algorithms.com/", ResourceType.ARTICLE, "en", "CP-Algorithms")
            );
        }
        if (containsAny(fingerprint, "machine learning", "ml", "ai", "llm", "embedding", "нейрос")) {
            return List.of(
                    resource("Учебник по машинному обучению", "https://education.yandex.ru/handbook/ml", ResourceType.COURSE, "ru", "Яндекс"),
                    resource("Machine Learning на Habr", "https://habr.com/ru/hubs/machine_learning/articles/", ResourceType.ARTICLE, "ru", "Habr"),
                    resource("Deep Learning Book", "https://www.deeplearningbook.org/", ResourceType.BOOK, "en", "Deep Learning Book")
            );
        }
        if (containsAny(fingerprint, "android", "kotlin")) {
            return List.of(
                    resource("Android Kotlin на русском", "https://developer.android.com/kotlin?hl=ru", ResourceType.ARTICLE, "ru", "Android"),
                    resource("Курсы Android", "https://developer.android.com/courses?hl=ru", ResourceType.COURSE, "ru", "Android"),
                    resource("Kotlin Documentation", "https://kotlinlang.org/docs/home.html", ResourceType.ARTICLE, "en", "Kotlin")
            );
        }
        if (containsAny(fingerprint, "swift", "ios", "swiftui")) {
            return List.of(
                    resource("SwiftBook", "https://swiftbook.org/", ResourceType.COURSE, "ru", "SwiftBook"),
                    resource("Swift Documentation", "https://www.swift.org/documentation/", ResourceType.ARTICLE, "en", "Swift"),
                    resource("Apple Developer Documentation", "https://developer.apple.com/documentation/", ResourceType.ARTICLE, "en", "Apple")
            );
        }
        if (containsAny(fingerprint, "flutter", "react native", "mobile")) {
            return List.of(
                    resource("Flutter Documentation", "https://docs.flutter.dev/", ResourceType.ARTICLE, "en", "Flutter"),
                    resource("React Native Documentation", "https://reactnative.dev/docs/getting-started", ResourceType.ARTICLE, "en", "React Native"),
                    resource("Mobile development на Habr", "https://habr.com/ru/search/?q=mobile%20development&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        if (containsAny(fingerprint, "ux", "ui", "design", "product", "research")) {
            return List.of(
                    resource("UX/UI: материалы на Habr", "https://habr.com/ru/search/?q=UX%20UI&target_type=posts", ResourceType.ARTICLE, "ru", "Habr"),
                    resource("Material Design", "https://m3.material.io/", ResourceType.ARTICLE, "en", "Google"),
                    resource("Nielsen Norman Group", "https://www.nngroup.com/articles/", ResourceType.ARTICLE, "en", "NN/g")
            );
        }
        if (containsAny(fingerprint, "system design", "architecture", "microservice", "distributed")) {
            return List.of(
                    resource("System Design Primer", "https://github.com/donnemartin/system-design-primer", ResourceType.BOOK, "en", "GitHub"),
                    resource("Архитектура ПО на Habr", "https://habr.com/ru/search/?q=архитектура%20ПО&target_type=posts", ResourceType.ARTICLE, "ru", "Habr"),
                    resource("Microservices.io", "https://microservices.io/patterns/", ResourceType.ARTICLE, "en", "Microservices.io")
            );
        }
        if (containsAny(fingerprint, "cloud", "aws", "terraform", "devops", "ci/cd", "cicd")) {
            return List.of(
                    resource("Terraform Documentation", "https://developer.hashicorp.com/terraform/docs", ResourceType.ARTICLE, "en", "HashiCorp"),
                    resource("AWS Skill Builder", "https://skillbuilder.aws/", ResourceType.COURSE, "en", "AWS"),
                    resource("DevOps на Habr", "https://habr.com/ru/search/?q=DevOps&target_type=posts", ResourceType.ARTICLE, "ru", "Habr")
            );
        }
        return List.of();
    }

    private List<FallbackResourceTemplate> searchFallbackResources(String query, String localizedTopicTitle) {
        String encodedRuQuery = encode(query + " обучение на русском");
        return List.of(
                resource(
                        "Habr: материалы по теме «" + localizedTopicTitle + "»",
                        "https://habr.com/ru/search/?q=" + encodedRuQuery + "&target_type=posts",
                        ResourceType.ARTICLE,
                        "ru",
                        "Habr"
                ),
                resource(
                        "YouTube: разборы на русском по теме «" + localizedTopicTitle + "»",
                        "https://www.youtube.com/results?search_query=" + encodedRuQuery,
                        ResourceType.VIDEO,
                        "ru",
                        "YouTube"
                ),
                resource(
                        "Tproger: статьи и подборки по теме «" + localizedTopicTitle + "»",
                        "https://tproger.ru/search?q=" + encodedRuQuery,
                        ResourceType.ARTICLE,
                        "ru",
                        "Tproger"
                ),
                resource(
                        "Proglib: практические материалы по теме «" + localizedTopicTitle + "»",
                        "https://proglib.io/search?q=" + encodedRuQuery,
                        ResourceType.ARTICLE,
                        "ru",
                        "Proglib"
                ),
                resource(
                        "Stepik: русскоязычные курсы по теме «" + localizedTopicTitle + "»",
                        "https://stepik.org/catalog/search?q=" + encodedRuQuery,
                        ResourceType.COURSE,
                        "ru",
                        "Stepik"
                )
        );
    }

    private FallbackResourceTemplate resource(String title,
                                              String url,
                                              ResourceType type,
                                              String language,
                                              String provider) {
        return new FallbackResourceTemplate(title, url, type, language, provider);
    }

    private String normalizeResourceFingerprint(Topic topic, String localizedTopicTitle) {
        return (String.join(" ",
                topic.getCode() != null ? topic.getCode() : "",
                topic.getTitle() != null ? topic.getTitle() : "",
                localizedTopicTitle != null ? localizedTopicTitle : "",
                topic.getDescription() != null ? topic.getDescription() : ""
        ))
                .toLowerCase()
                .replace('_', ' ')
                .replace('-', ' ');
    }

    private boolean containsAny(String value, String... markers) {
        for (String marker : markers) {
            if (value.contains(marker)) {
                return true;
            }
        }
        return false;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
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
