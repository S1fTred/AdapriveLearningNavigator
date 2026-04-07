package com.example.adaprivelearningnavigator.ai.prompt;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AiPromptBuilderTest {

    private final AiPromptBuilder promptBuilder = new AiPromptBuilder();

    @Test
    void systemPromptShouldRequireOnlyJson() {
        String prompt = promptBuilder.buildSystemPrompt();

        assertAll(
                () -> assertTrue(prompt.contains("ТОЛЬКО JSON")),
                () -> assertTrue(prompt.contains("Любой текст вне JSON запрещён")),
                () -> assertTrue(prompt.contains("Не используй markdown")),
                () -> assertTrue(prompt.contains("\"topics\": []"))
        );
    }

    @Test
    void userPromptShouldContainRequestFields() {
        AiPlanGenerateRequest request = new AiPlanGenerateRequest(
                "Java backend developer",
                UserLevel.BEGINNER,
                8,
                Set.of("Git", "HTTP")
        );

        String prompt = promptBuilder.buildUserPrompt(request);

        assertAll(
                () -> assertTrue(prompt.contains("Java backend developer")),
                () -> assertTrue(prompt.contains("BEGINNER")),
                () -> assertTrue(prompt.contains("Git")),
                () -> assertTrue(prompt.contains("HTTP")),
                () -> assertTrue(prompt.contains("8"))
        );
    }
}
