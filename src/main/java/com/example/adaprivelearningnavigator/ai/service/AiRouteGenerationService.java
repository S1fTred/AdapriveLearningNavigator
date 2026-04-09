package com.example.adaprivelearningnavigator.ai.service;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;
import com.example.adaprivelearningnavigator.ai.dto.AiTopicScopeItemDto;

import java.util.List;

public interface AiRouteGenerationService {
    AiRouteGenerateResponse generateRoute(AiPlanGenerateRequest request, List<AiTopicScopeItemDto> topicScope);
}
