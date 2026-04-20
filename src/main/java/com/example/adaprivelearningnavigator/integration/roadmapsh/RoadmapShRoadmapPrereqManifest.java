package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;

public record RoadmapShRoadmapPrereqManifest(
        String topicCode,
        PrereqRelationType relationType
) {
}
