package com.example.adaprivelearningnavigator.ai.validation;

import com.example.adaprivelearningnavigator.ai.dto.AiGeneratedTopicDto;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.service.exception.AiRouteValidationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AiRouteValidationService {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "and", "for", "in", "of", "on", "the", "to",
            "в", "во", "для", "и", "или", "на", "по", "с", "со"
    );

    private static final Set<String> GENERIC_TOKENS = Set.of(
            "basics", "concept", "concepts", "intro", "principle", "principles"
    );

    private static final Map<String, String> TOKEN_ALIASES = Map.ofEntries(
            Map.entry("basic", "basics"),
            Map.entry("basics", "basics"),
            Map.entry("concept", "concepts"),
            Map.entry("concepts", "concepts"),
            Map.entry("foundation", "basics"),
            Map.entry("foundations", "basics"),
            Map.entry("fundamental", "basics"),
            Map.entry("fundamentals", "basics"),
            Map.entry("intro", "intro"),
            Map.entry("introduction", "intro"),
            Map.entry("overview", "intro"),
            Map.entry("principle", "principles"),
            Map.entry("principles", "principles"),
            Map.entry("база", "basics"),
            Map.entry("базовые", "basics"),
            Map.entry("базовый", "basics"),
            Map.entry("введение", "intro"),
            Map.entry("основы", "basics"),
            Map.entry("концепции", "concepts"),
            Map.entry("концепция", "concepts"),
            Map.entry("принципы", "principles"),
            Map.entry("принцип", "principles"),

            Map.entry("git", "git"),
            Map.entry("http", "http"),
            Map.entry("hibernate", "hibernate"),
            Map.entry("java", "java"),
            Map.entry("jpa", "jpa"),
            Map.entry("maven", "maven"),
            Map.entry("oop", "oop"),
            Map.entry("rest", "rest"),
            Map.entry("spring", "spring"),
            Map.entry("boot", "boot"),
            Map.entry("sql", "sql"),

            Map.entry("гит", "git"),
            Map.entry("джава", "java"),
            Map.entry("ява", "java"),
            Map.entry("мавен", "maven"),
            Map.entry("ооп", "oop"),
            Map.entry("рест", "rest"),
            Map.entry("спринг", "spring"),
            Map.entry("бут", "boot"),
            Map.entry("хибернейт", "hibernate"),

            Map.entry("object", "oop"),
            Map.entry("oriented", "oop"),
            Map.entry("objectoriented", "oop"),
            Map.entry("объектно", "oop"),
            Map.entry("объектноориентированное", "oop"),
            Map.entry("ориентированное", "oop"),

            Map.entry("framework", "framework"),
            Map.entry("frameworks", "framework"),
            Map.entry("фреймворк", "framework"),
            Map.entry("фреймворки", "framework"),

            Map.entry("collection", "collections"),
            Map.entry("collections", "collections"),
            Map.entry("коллекции", "collections"),
            Map.entry("коллекция", "collections"),

            Map.entry("programming", ""),
            Map.entry("programmer", ""),
            Map.entry("development", ""),
            Map.entry("разработка", ""),
            Map.entry("разработки", ""),
            Map.entry("программирование", ""),
            Map.entry("программирования", "")
    );

    private final TopicRepository topicRepository;

    public AiRouteValidationService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void validateGeneratedRoute(AiRouteGenerateResponse response) {
        Map<String, Long> duplicates = response.topics().stream()
                .map(this::duplicateKey)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        boolean hasDuplicates = duplicates.values().stream().anyMatch(count -> count > 1);
        if (hasDuplicates) {
            throw new AiRouteValidationException("AI вернул дублирующиеся темы");
        }

        for (AiGeneratedTopicDto topic : response.topics()) {
            if (!StringUtils.hasText(topic.topicCode())) {
                throw new AiRouteValidationException("AI вернул тему без topicCode");
            }
            if (!StringUtils.hasText(topic.title())) {
                throw new AiRouteValidationException("AI вернул тему без названия");
            }
            if (!StringUtils.hasText(topic.reason())) {
                throw new AiRouteValidationException("AI вернул тему без объяснения");
            }
            if (topic.priority() == null || topic.priority() <= 0) {
                throw new AiRouteValidationException("AI вернул некорректный приоритет темы");
            }
            if (topic.estimatedHours() == null || topic.estimatedHours().signum() <= 0) {
                throw new AiRouteValidationException("AI вернул некорректную длительность темы");
            }
        }
    }

    public Topic resolveExistingTopic(AiGeneratedTopicDto generatedTopic) {
        if (StringUtils.hasText(generatedTopic.topicCode())) {
            List<Topic> codeMatches = topicRepository.findAll().stream()
                    .filter(topic -> StringUtils.hasText(topic.getCode()))
                    .filter(topic -> topic.getCode().equalsIgnoreCase(generatedTopic.topicCode()))
                    .toList();

            if (codeMatches.size() == 1) {
                return codeMatches.get(0);
            }
            if (codeMatches.size() > 1) {
                throw new AiRouteValidationException("Найдено несколько тем по коду: " + generatedTopic.topicCode());
            }
        }

        return resolveExistingTopic(generatedTopic.title());
    }

    public Topic resolveExistingTopic(String value) {
        List<Topic> allTopics = topicRepository.findAll();

        List<Topic> exactCodeMatches = allTopics.stream()
                .filter(topic -> topic.getCode() != null && topic.getCode().equalsIgnoreCase(value))
                .toList();
        if (exactCodeMatches.size() == 1) {
            return exactCodeMatches.get(0);
        }
        if (exactCodeMatches.size() > 1) {
            throw new AiRouteValidationException("Найдено несколько тем по коду: " + value);
        }

        List<Topic> exactTitleMatches = allTopics.stream()
                .filter(topic -> topic.getTitle() != null && topic.getTitle().equalsIgnoreCase(value))
                .toList();
        if (exactTitleMatches.size() == 1) {
            return exactTitleMatches.get(0);
        }
        if (exactTitleMatches.size() > 1) {
            throw new AiRouteValidationException("Найдено несколько тем по названию: " + value);
        }

        String normalizedValue = normalize(value);
        List<Topic> containsMatches = allTopics.stream()
                .filter(topic -> StringUtils.hasText(topic.getTitle()))
                .filter(topic -> normalize(topic.getTitle()).contains(normalizedValue))
                .sorted(Comparator.comparing(Topic::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();

        if (containsMatches.size() == 1) {
            return containsMatches.get(0);
        }
        if (containsMatches.size() > 1) {
            throw new AiRouteValidationException("Найдено несколько тем по частичному совпадению: " + value);
        }

        Set<String> requestedTokens = canonicalTokens(value);
        if (requestedTokens.isEmpty()) {
            throw new AiRouteValidationException("Тема не найдена в базе знаний: " + value);
        }

        List<Topic> semanticMatches = allTopics.stream()
                .filter(topic -> canonicalTokens(topic).equals(requestedTokens))
                .sorted(Comparator.comparing(Topic::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();
        if (semanticMatches.size() == 1) {
            return semanticMatches.get(0);
        }
        if (semanticMatches.size() > 1) {
            throw new AiRouteValidationException("Найдено несколько тем по семантическому совпадению: " + value);
        }

        Set<String> requestedCoreTokens = coreTokens(requestedTokens);
        if (!requestedCoreTokens.isEmpty()) {
            List<Topic> coreMatches = allTopics.stream()
                    .filter(topic -> coreTokens(canonicalTokens(topic)).equals(requestedCoreTokens))
                    .sorted(Comparator.comparing(Topic::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .toList();

            if (coreMatches.size() == 1) {
                return coreMatches.get(0);
            }
            if (coreMatches.size() > 1) {
                throw new AiRouteValidationException("Найдено несколько тем по смысловому совпадению: " + value);
            }
        }

        throw new AiRouteValidationException("Тема не найдена в базе знаний: " + value);
    }

    private String duplicateKey(AiGeneratedTopicDto topic) {
        if (StringUtils.hasText(topic.topicCode())) {
            return "code:" + normalize(topic.topicCode());
        }
        return "title:" + normalize(topic.title());
    }

    private Set<String> canonicalTokens(Topic topic) {
        return canonicalTokens(topic.getCode() + " " + topic.getTitle());
    }

    private Set<String> canonicalTokens(String value) {
        return Arrays.stream(normalize(value).split("[^\\p{IsAlphabetic}\\p{IsDigit}]+"))
                .filter(StringUtils::hasText)
                .map(this::canonicalizeToken)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<String> coreTokens(Set<String> tokens) {
        return tokens.stream()
                .filter(token -> !GENERIC_TOKENS.contains(token))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String canonicalizeToken(String rawToken) {
        String token = normalize(rawToken);
        if (!StringUtils.hasText(token) || STOP_WORDS.contains(token)) {
            return "";
        }

        String alias = TOKEN_ALIASES.getOrDefault(token, token);
        return STOP_WORDS.contains(alias) ? "" : alias;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
