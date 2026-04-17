package com.example.adaprivelearningnavigator.service.dto.roadmap;

import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import lombok.Builder;

@Builder
public record RoadmapResourceResponse(
        Long id,
        String title,
        String url,
        ResourceType type,
        String language,
        Integer durationMinutes,
        String provider,
        String difficulty,
        Integer rank
) {
}
