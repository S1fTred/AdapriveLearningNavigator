package com.example.adaprivelearningnavigator.ai.dto;

import java.math.BigDecimal;

public record AiTopicScopeItemDto(
        String topicCode,
        String title,
        String level,
        boolean core,
        BigDecimal estimatedHours
) {
}
