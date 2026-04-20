package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;

import java.math.BigDecimal;
import java.util.List;

public record RoadmapShRoadmapTopicManifest(
        String code,
        String title,
        String description,
        TopicLevel level,
        boolean core,
        BigDecimal estimatedHours,
        EntityStatus status,
        int priority,
        boolean required,
        List<RoadmapShRoadmapPrereqManifest> prereqs,
        List<RoadmapShRoadmapResourceManifest> resources
) {
}
