package com.example.adaprivelearningnavigator.ai.validation;

import com.example.adaprivelearningnavigator.ai.dto.AiGeneratedTopicDto;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.service.exception.AiRouteValidationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AiRouteValidationService {

    private final TopicRepository topicRepository;

    public AiRouteValidationService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void validateGeneratedRoute(AiRouteGenerateResponse response) {
        Map<String, Long> duplicates = response.topics().stream()
                .map(AiGeneratedTopicDto::title)
                .map(this::normalize)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        boolean hasDuplicates = duplicates.values().stream().anyMatch(count -> count > 1);
        if (hasDuplicates) {
            throw new AiRouteValidationException("AI вернул дублирующиеся темы");
        }

        for (AiGeneratedTopicDto topic : response.topics()) {
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

        throw new AiRouteValidationException("Тема не найдена в базе знаний: " + value);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
