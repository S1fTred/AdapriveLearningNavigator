package com.example.adaprivelearningnavigator.ai.prompt;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AiPromptBuilder {

    public String buildSystemPrompt() {
        return """
                You build a learning route for an IT goal using a fixed knowledge base provided by the backend.
                The response must contain ONLY JSON. Any text outside JSON is forbidden.
                Do not use markdown.
                Do not add explanations before or after JSON.
                Do not wrap JSON in ```json.

                You must select topics ONLY from the allowed knowledge base topics provided in the user message.
                Do not invent new topics.
                Do not invent new topic codes.
                For every selected topic:
                - topicCode must exactly match one of the allowed topic codes
                - title must exactly match the corresponding title from the allowed list
                - priority must be a positive integer
                - estimatedHours must be a positive number
                - reason must explain why the topic is useful for the goal
                - dependsOn may contain only topicCode values from the allowed list

                Return JSON strictly in this shape:
                {
                  "interpretedGoal": "string",
                  "topics": [
                    {
                      "topicCode": "JAVA_BASICS",
                      "title": "Java Basics",
                      "priority": 1,
                      "estimatedHours": 6,
                      "reason": "string",
                      "dependsOn": ["HTTP"]
                    }
                  ]
                }

                If you cannot build a correct route from the allowed knowledge base topics, still return valid JSON of the same shape.
                In that case use an empty topics list.
                Example fallback response:
                {
                  "interpretedGoal": "string",
                  "topics": []
                }
                """;
    }

    public String buildUserPrompt(AiPlanGenerateRequest request, List<AiTopicScopeItemDto> topicScope) {
        String knownTopics = request.knownTopics().isEmpty()
                ? "none"
                : request.knownTopics().stream().sorted().collect(Collectors.joining(", "));

        String scopeLines = topicScope.stream()
                .map(topic -> "- %s | %s | level=%s | core=%s | estimatedHours=%s".formatted(
                        topic.topicCode(),
                        topic.title(),
                        topic.level(),
                        topic.core(),
                        topic.estimatedHours() == null ? "unknown" : topic.estimatedHours().stripTrailingZeros().toPlainString()
                ))
                .collect(Collectors.joining("\n"));

        return """
                Build a learning route using only the allowed knowledge base topics.

                Goal: %s
                Current level: %s
                Known topics: %s
                Hours per week: %d

                Allowed knowledge base topics:
                %s

                Return only JSON in the required structure.
                """.formatted(
                request.goal(),
                request.currentLevel().name(),
                knownTopics,
                request.hoursPerWeek(),
                scopeLines
        );
    }
}
