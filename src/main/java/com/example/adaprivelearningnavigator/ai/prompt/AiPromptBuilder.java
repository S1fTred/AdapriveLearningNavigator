package com.example.adaprivelearningnavigator.ai.prompt;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

                You must build a COMPLETE ordered route using ONLY the allowed knowledge base topics provided in the user message.
                Do not invent new topics.
                Do not invent new topic codes.
                You must include every topic marked required=true unless it is marked known=true.
                If a selected topic has requiredPrereqCodes, every such prerequisite must appear earlier in the route unless it is marked known=true.
                Do not include topics marked known=true in the output route.
                Order the route from foundational to advanced topics.
                Before answering, verify:
                - all required topics are included unless known=true
                - no topic outside the allowed list is included
                - prerequisites are placed before dependent topics
                - lower priority number means earlier topic in the route

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
        return buildUserPrompt(request, topicScope, null);
    }

    public String buildUserPrompt(AiPlanGenerateRequest request,
                                  List<AiTopicScopeItemDto> topicScope,
                                  String correctionFeedback) {
        String knownTopics = request.knownTopics().isEmpty()
                ? "none"
                : request.knownTopics().stream().sorted().collect(Collectors.joining(", "));

        String scopeLines = topicScope.stream()
                .map(topic -> "- code=%s | title=%s | level=%s | rolePriority=%s | required=%s | known=%s | core=%s | estimatedHours=%s | requiredPrereqCodes=%s".formatted(
                        topic.topicCode(),
                        topic.title(),
                        topic.level(),
                        topic.rolePriority() == null ? "none" : topic.rolePriority(),
                        topic.required(),
                        topic.known(),
                        topic.core(),
                        topic.estimatedHours() == null ? "unknown" : topic.estimatedHours().stripTrailingZeros().toPlainString(),
                        topic.requiredPrereqCodes().isEmpty() ? "[]" : topic.requiredPrereqCodes()
                ))
                .collect(Collectors.joining("\n"));

        String correctionBlock = StringUtils.hasText(correctionFeedback)
                ? """

                Previous route validation issues that must be fixed now:
                %s
                """.formatted(correctionFeedback)
                : "";

        return """
                Build a learning route using only the allowed knowledge base topics.
                Return a COMPLETE route, not a partial recommendation.

                Goal: %s
                Current level: %s
                Known topics: %s
                Hours per week: %d

                Required behavior:
                - include every required topic unless known=true
                - include prerequisites before dependent topics unless known=true
                - exclude topics marked known=true from the final route
                - use exact topicCode and exact title from the allowed topic list

                Allowed knowledge base topics:
                %s
                %s

                Return only JSON in the required structure.
                """.formatted(
                request.goal(),
                request.currentLevel().name(),
                knownTopics,
                request.hoursPerWeek(),
                scopeLines,
                correctionBlock
        );
    }
}
