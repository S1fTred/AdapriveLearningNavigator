package com.example.adaprivelearningnavigator.ai.prompt;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AiPromptBuilder {

    public String buildSystemPrompt() {
        return """
                Ты помогаешь строить маршрут обучения для IT-цели.
                Ответ должен содержать ТОЛЬКО JSON. Любой текст вне JSON запрещён.
                Не используй markdown.
                Не добавляй пояснения до JSON или после JSON.
                Не оборачивай JSON в ```json.

                Верни JSON строго такого вида:
                {
                  "interpretedGoal": "string",
                  "topics": [
                    {
                      "title": "string",
                      "priority": 1,
                      "estimatedHours": 2.5,
                      "reason": "string",
                      "dependsOn": ["string"]
                    }
                  ]
                }

                Если ты не можешь корректно построить маршрут, всё равно верни валидный JSON той же структуры.
                В таком случае используй пустой список topics.
                Пример допустимого fallback-ответа:
                {
                  "interpretedGoal": "string",
                  "topics": []
                }
                """;
    }

    public String buildUserPrompt(AiPlanGenerateRequest request) {
        String knownTopics = request.knownTopics().isEmpty()
                ? "нет"
                : request.knownTopics().stream().sorted().collect(Collectors.joining(", "));

        return """
                Построй маршрут обучения.

                Цель: %s
                Текущий уровень: %s
                Известные темы: %s
                Часы в неделю: %d

                Верни только JSON в указанной структуре.
                """.formatted(
                request.goal(),
                request.currentLevel().name(),
                knownTopics,
                request.hoursPerWeek()
        );
    }
}
