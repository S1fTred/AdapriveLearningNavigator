package com.example.adaprivelearningnavigator.config;

@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "app.roadmap-sh.roadmap-sync")
public record RoadmapShRoadmapSyncProperties(
        boolean enabled,
        String manifestPattern,
        boolean updateExisting
) {
    public RoadmapShRoadmapSyncProperties {
        if (manifestPattern == null || manifestPattern.isBlank()) {
            manifestPattern = "classpath*:roadmap-sh/roadmaps/*.json";
        }
    }
}
