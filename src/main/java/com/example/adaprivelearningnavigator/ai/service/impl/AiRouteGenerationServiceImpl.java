package com.example.adaprivelearningnavigator.ai.service.impl;

import com.example.adaprivelearningnavigator.ai.dto.AiGeneratedTopicDto;
import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;
import com.example.adaprivelearningnavigator.ai.prompt.AiPromptBuilder;
import com.example.adaprivelearningnavigator.ai.service.AiRouteGenerationService;
import com.example.adaprivelearningnavigator.service.exception.AiRouteGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AiRouteGenerationServiceImpl implements AiRouteGenerationService {

    private static final Logger log = LoggerFactory.getLogger(AiRouteGenerationServiceImpl.class);

    private final ChatClient chatClient;
    private final AiPromptBuilder promptBuilder;
    private final ObjectMapper objectMapper;

    public AiRouteGenerationServiceImpl(ChatClient.Builder chatClientBuilder,
                                        AiPromptBuilder promptBuilder,
                                        ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.promptBuilder = promptBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public AiRouteGenerateResponse generateRoute(AiPlanGenerateRequest request, List<AiTopicScopeItemDto> topicScope) {
        String systemPrompt = promptBuilder.buildSystemPrompt();
        String userPrompt = promptBuilder.buildUserPrompt(request, topicScope);
        String rawResponse;

        log.debug("AI system prompt: {}", systemPrompt);
        log.debug("AI user prompt: {}", userPrompt);

        try {
            rawResponse = chatClient.prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();
        } catch (Exception ex) {
            log.error("Ошибка вызова AI для генерации маршрута", ex);
            throw new AiRouteGenerationException("Не удалось получить ответ от AI", ex);
        }

        log.debug("Raw AI response: {}", rawResponse);

        if (!StringUtils.hasText(rawResponse)) {
            throw new AiRouteGenerationException("AI вернул пустой ответ");
        }

        try {
            AiRouteGenerateResponse response = objectMapper.readValue(rawResponse, AiRouteGenerateResponse.class);
            validateStructure(response);
            return response;
        } catch (AiRouteGenerationException ex) {
            log.warn("AI вернул структурно некорректный ответ: {}", rawResponse);
            throw ex;
        } catch (Exception ex) {
            log.warn("Не удалось распарсить ответ AI: {}", rawResponse);
            throw new AiRouteGenerationException("AI вернул некорректный JSON", ex);
        }
    }

    private void validateStructure(AiRouteGenerateResponse response) {
        if (response == null || !StringUtils.hasText(response.interpretedGoal())) {
            throw new AiRouteGenerationException("AI вернул некорректную структуру ответа");
        }
        if (response.topics() == null || response.topics().isEmpty()) {
            throw new AiRouteGenerationException("AI не вернул список тем");
        }
        List<AiGeneratedTopicDto> topics = response.topics();
        for (AiGeneratedTopicDto topic : topics) {
            if (topic == null
                    || !StringUtils.hasText(topic.topicCode())
                    || !StringUtils.hasText(topic.title())
                    || topic.priority() == null
                    || topic.estimatedHours() == null
                    || !StringUtils.hasText(topic.reason())) {
                throw new AiRouteGenerationException("AI вернул неполную структуру темы");
            }
        }
    }
}
