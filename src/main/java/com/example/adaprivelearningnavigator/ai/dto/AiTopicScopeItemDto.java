package com.example.adaprivelearningnavigator.ai.dto;

import java.math.BigDecimal;

public record AiTopicScopeItemDto(
        String topicCode,
        String title,
        String level,
        Integer rolePriority,
        boolean required,
        boolean known,
        boolean core,
        BigDecimal estimatedHours,
        java.util.List<String> requiredPrereqCodes
) {
}
