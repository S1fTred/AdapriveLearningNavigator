package com.example.adaprivelearningnavigator.ai.prompt;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;
import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiPromptBuilderTest {

    private final AiPromptBuilder promptBuilder = new AiPromptBuilder();

    @Test
    void systemPromptShouldRequireOnlyJsonAndTopicCode() {
        String prompt = promptBuilder.buildSystemPrompt();

        assertAll(
                () -> assertTrue(prompt.contains("ONLY JSON")),
                () -> assertTrue(prompt.contains("Any text outside JSON is forbidden")),
                () -> assertTrue(prompt.contains("Do not use markdown")),
                () -> assertTrue(prompt.contains("\"topicCode\": \"JAVA_BASICS\"")),
                () -> assertTrue(prompt.contains("\"topics\": []"))
        );
    }

    @Test
    void userPromptShouldContainRequestFieldsAndAllowedKbTopics() {
        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "Java backend developer",
                UserLevel.BEGINNER,
                8,
                Set.of("Git", "HTTP")
        );
        List<AiTopicScopeItemDto> scope = List.of(
                new AiTopicScopeItemDto("JAVA_BASICS", "Java Basics", "BASIC", true, BigDecimal.valueOf(6)),
                new AiTopicScopeItemDto("OOP_JAVA", "OOP in Java", "BASIC", true, BigDecimal.valueOf(5))
        );

        String prompt = promptBuilder.buildUserPrompt(request, scope);

        assertAll(
                () -> assertTrue(prompt.contains("Java backend developer")),
                () -> assertTrue(prompt.contains("BEGINNER")),
                () -> assertTrue(prompt.contains("Git")),
                () -> assertTrue(prompt.contains("HTTP")),
                () -> assertTrue(prompt.contains("8")),
                () -> assertTrue(prompt.contains("JAVA_BASICS")),
                () -> assertTrue(prompt.contains("Java Basics")),
                () -> assertTrue(prompt.contains("OOP_JAVA")),
                () -> assertTrue(prompt.contains("OOP in Java"))
        );
    }
}
