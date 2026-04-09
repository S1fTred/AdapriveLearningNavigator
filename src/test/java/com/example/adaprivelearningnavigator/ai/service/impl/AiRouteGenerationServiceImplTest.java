package com.example.adaprivelearningnavigator.ai.service.impl;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;
import com.example.adaprivelearningnavigator.ai.prompt.AiPromptBuilder;
import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import com.example.adaprivelearningnavigator.service.exception.AiRouteGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AiRouteGenerationServiceImplTest {

    @Test
    void shouldParseValidJsonResponse() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content())
                .thenReturn("""
                        {
                          "interpretedGoal": "Java backend developer",
                          "topics": [
                            {
                              "topicCode": "SPRING_BOOT",
                              "title": "Spring Boot",
                              "priority": 1,
                              "estimatedHours": 6,
                              "reason": "Core framework",
                              "dependsOn": ["HTTP"]
                            }
                          ]
                        }
                        """);

        AiRouteGenerationServiceImpl service = new AiRouteGenerationServiceImpl(
                builder,
                new AiPromptBuilder(),
                new ObjectMapper()
        );

        AiRouteGenerateResponse response = service.generateRoute(validRequest(), topicScope());

        assertEquals("Java backend developer", response.interpretedGoal());
        assertEquals(1, response.topics().size());
        assertEquals("SPRING_BOOT", response.topics().get(0).topicCode());
        assertEquals("Spring Boot", response.topics().get(0).title());
    }

    @Test
    void shouldFailOnInvalidJson() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content())
                .thenReturn("not-json");

        AiRouteGenerationServiceImpl service = new AiRouteGenerationServiceImpl(
                builder,
                new AiPromptBuilder(),
                new ObjectMapper()
        );

        assertThrows(AiRouteGenerationException.class, () -> service.generateRoute(validRequest(), topicScope()));
    }

    @Test
    void shouldFailOnEmptyTopics() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content())
                .thenReturn("""
                        {
                          "interpretedGoal": "Java backend developer",
                          "topics": []
                        }
                        """);

        AiRouteGenerationServiceImpl service = new AiRouteGenerationServiceImpl(
                builder,
                new AiPromptBuilder(),
                new ObjectMapper()
        );

        assertThrows(AiRouteGenerationException.class, () -> service.generateRoute(validRequest(), topicScope()));
    }

    @Test
    void shouldFailOnMissingTopicCode() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content())
                .thenReturn("""
                        {
                          "interpretedGoal": "Java backend developer",
                          "topics": [
                            {
                              "title": "Spring Boot",
                              "priority": 1,
                              "estimatedHours": 6,
                              "reason": "Core framework",
                              "dependsOn": []
                            }
                          ]
                        }
                        """);

        AiRouteGenerationServiceImpl service = new AiRouteGenerationServiceImpl(
                builder,
                new AiPromptBuilder(),
                new ObjectMapper()
        );

        assertThrows(AiRouteGenerationException.class, () -> service.generateRoute(validRequest(), topicScope()));
    }

    @Test
    void shouldFailWhenChatClientThrows() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(anyString()).call().content())
                .thenThrow(new RuntimeException("Ollama unavailable"));

        AiRouteGenerationServiceImpl service = new AiRouteGenerationServiceImpl(
                builder,
                new AiPromptBuilder(),
                new ObjectMapper()
        );

        assertThrows(AiRouteGenerationException.class, () -> service.generateRoute(validRequest(), topicScope()));
    }

    private AiPlanGenerateRequest validRequest() {
        return new AiPlanGenerateRequest(
                "Java backend developer",
                UserLevel.BEGINNER,
                8,
                Set.of("Git")
        );
    }

    private List<AiTopicScopeItemDto> topicScope() {
        return List.of(
                new AiTopicScopeItemDto("JAVA_BASICS", "Java Basics", "BASIC", true, BigDecimal.valueOf(6)),
                new AiTopicScopeItemDto("SPRING_BOOT", "Spring Boot", "INTERMEDIATE", true, BigDecimal.valueOf(8))
        );
    }
}
