package com.example.adaprivelearningnavigator.service.dto.roadmap;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record RoadmapDetailResponse(
        Long id,
        String code,
        String name,
        String description,
        int topicCount,
        int requiredTopicCount,
        BigDecimal totalEstimatedHours,
        List<RoadmapTopicResponse> topics
) {
}
