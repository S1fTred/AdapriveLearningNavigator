package com.example.adaprivelearningnavigator.ai.validation;

import com.example.adaprivelearningnavigator.ai.dto.AiGeneratedTopicDto;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.service.exception.AiRouteValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiRouteValidationServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Test
    void shouldRejectDuplicateTopicsIgnoringCase() {
        AiRouteValidationService service = new AiRouteValidationService(topicRepository);
        AiRouteGenerateResponse response = new AiRouteGenerateResponse(
                "Java backend",
                List.of(
                        generatedTopic("SPRING_BOOT", "Spring Boot", 1, 6, "Core"),
                        generatedTopic("spring_boot", "Spring Boot", 2, 4, "Duplicate")
                )
        );

        assertThrows(AiRouteValidationException.class, () -> service.validateGeneratedRoute(response));
    }

    @Test
    void shouldResolveTopicByTopicCodeFirst() {
        Topic javaBasics = topic(10L, "JAVA_BASICS", "Java Basics");
        Topic springBoot = topic(11L, "SPRING_BOOT", "Spring Boot");
        when(topicRepository.findAll()).thenReturn(List.of(javaBasics, springBoot));

        AiRouteValidationService service = new AiRouteValidationService(topicRepository);

        assertEquals(springBoot, service.resolveExistingTopic(generatedTopic("SPRING_BOOT", "Any title", 1, 6, "Reason")));
        assertEquals(javaBasics, service.resolveExistingTopic("JAVA_BASICS"));
        assertEquals(springBoot, service.resolveExistingTopic("Spring Boot"));
        assertEquals(springBoot, service.resolveExistingTopic("Boot"));
    }

    @Test
    void shouldResolveRussianEquivalentToEnglishTopicBySemanticTokens() {
        Topic javaBasics = topic(10L, "JAVA_BASICS", "Java Basics");
        Topic oop = topic(11L, "OOP_JAVA", "OOP in Java");
        when(topicRepository.findAll()).thenReturn(List.of(javaBasics, oop));

        AiRouteValidationService service = new AiRouteValidationService(topicRepository);

        assertEquals(javaBasics, service.resolveExistingTopic("Основы программирования на Java"));
        assertEquals(oop, service.resolveExistingTopic("Объектно-ориентированное программирование на Java"));
        assertEquals(oop, service.resolveExistingTopic("Object-oriented programming (OOP) concepts in Java"));
    }

    @Test
    void shouldResolveByCoreMeaningWhenDifferenceIsOnlyGenericWord() {
        Topic git = topic(12L, "GIT", "Git");
        when(topicRepository.findAll()).thenReturn(List.of(git));

        AiRouteValidationService service = new AiRouteValidationService(topicRepository);

        assertEquals(git, service.resolveExistingTopic("Основы Git"));
    }

    @Test
    void shouldRejectAmbiguousContainsMatch() {
        Topic springBoot = topic(11L, "SPRING_BOOT", "Spring Boot");
        Topic springSecurity = topic(12L, "SPRING_SECURITY", "Spring Security");
        when(topicRepository.findAll()).thenReturn(List.of(springBoot, springSecurity));

        AiRouteValidationService service = new AiRouteValidationService(topicRepository);

        assertThrows(AiRouteValidationException.class, () -> service.resolveExistingTopic("spring"));
    }

    @Test
    void shouldRejectAmbiguousSemanticMeaning() {
        Topic javaBasics = topic(10L, "JAVA_BASICS", "Java Basics");
        Topic oop = topic(11L, "OOP_JAVA", "OOP in Java");
        when(topicRepository.findAll()).thenReturn(List.of(javaBasics, oop));

        AiRouteValidationService service = new AiRouteValidationService(topicRepository);

        assertThrows(AiRouteValidationException.class, () -> service.resolveExistingTopic("Java"));
    }

    @Test
    void shouldRejectUnknownTopic() {
        when(topicRepository.findAll()).thenReturn(List.of(topic(10L, "JAVA_BASICS", "Java Basics")));

        AiRouteValidationService service = new AiRouteValidationService(topicRepository);

        assertThrows(AiRouteValidationException.class, () -> service.resolveExistingTopic("Kubernetes"));
    }

    private AiGeneratedTopicDto generatedTopic(String topicCode, String title, int priority, int hours, String reason) {
        return new AiGeneratedTopicDto(
                topicCode,
                title,
                priority,
                BigDecimal.valueOf(hours),
                reason,
                List.of()
        );
    }

    private Topic topic(Long id, String code, String title) {
        Topic topic = new Topic();
        topic.setId(id);
        topic.setCode(code);
        topic.setTitle(title);
        return topic;
    }
}
