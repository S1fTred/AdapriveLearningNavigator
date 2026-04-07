package com.example.adaprivelearningnavigator.domain.enums;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserLevelJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeUserLevelCaseInsensitively() throws Exception {
        String json = """
                {
                  "goal": "Java backend developer",
                  "currentLevel": "beginner",
                  "hoursPerWeek": 10,
                  "knownTopics": ["Git", "HTTP"]
                }
                """;

        AiPlanGenerateRequest request = objectMapper.readValue(json, AiPlanGenerateRequest.class);

        assertEquals(UserLevel.BEGINNER, request.currentLevel());
    }
}
