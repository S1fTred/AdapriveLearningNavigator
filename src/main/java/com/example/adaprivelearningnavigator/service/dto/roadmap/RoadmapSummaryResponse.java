package com.example.adaprivelearningnavigator.service.dto.roadmap;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RoadmapSummaryResponse(
        Long id,
        String code,
        String name,
        String category,
        String categoryLabel,
        String description,
        int topicCount,
        int requiredTopicCount,
        BigDecimal totalEstimatedHours
) {
}
