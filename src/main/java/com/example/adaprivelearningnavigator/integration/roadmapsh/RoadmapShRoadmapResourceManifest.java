package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;

public record RoadmapShRoadmapResourceManifest(
        String title,
        String url,
        ResourceType type,
        String language,
        Integer durationMinutes,
        String provider,
        String difficulty,
        EntityStatus status,
        int rank
) {
}
