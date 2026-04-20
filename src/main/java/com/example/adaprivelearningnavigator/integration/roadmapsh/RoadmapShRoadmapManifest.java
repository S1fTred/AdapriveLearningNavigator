package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;

import java.util.List;

public record RoadmapShRoadmapManifest(
        String source,
        String sourceUrl,
        String roleCode,
        String roleName,
        String roleDescription,
        EntityStatus roleStatus,
        List<RoadmapShRoadmapTopicManifest> topics
) {
}
