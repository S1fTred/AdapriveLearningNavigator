package com.example.adaprivelearningnavigator.ai.dto;

import java.util.List;

public record AiRouteGenerateResponse(
        String interpretedGoal,
        List<AiGeneratedTopicDto> topics
) {
}
