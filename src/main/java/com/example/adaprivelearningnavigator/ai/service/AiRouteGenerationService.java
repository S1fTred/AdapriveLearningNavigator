package com.example.adaprivelearningnavigator.ai.service;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiRouteGenerateResponse;

public interface AiRouteGenerationService {
    AiRouteGenerateResponse generateRoute(AiPlanGenerateRequest request);
}
