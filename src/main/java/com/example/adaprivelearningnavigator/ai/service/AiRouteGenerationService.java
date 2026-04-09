package com.example.adaprivelearningnavigator.ai.service;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;

import java.util.List;

public interface AiRouteGenerationService {
    default AiRouteGenerateResponse generateRoute(AiPlanGenerateRequest request, List<AiTopicScopeItemDto> topicScope) {
        return generateRoute(request, topicScope, null);
    }

    AiRouteGenerateResponse generateRoute(AiPlanGenerateRequest request,
                                          List<AiTopicScopeItemDto> topicScope,
                                          String correctionFeedback);
}
