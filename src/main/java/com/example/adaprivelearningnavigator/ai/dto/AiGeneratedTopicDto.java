package com.example.adaprivelearningnavigator.ai.dto;

import java.math.BigDecimal;
import java.util.List;

public record AiGeneratedTopicDto(
        String topicCode,
        String title,
        Integer priority,
        BigDecimal estimatedHours,
        String reason,
        List<String> dependsOn
) {
}
