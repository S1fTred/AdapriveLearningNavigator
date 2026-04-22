package com.example.adaprivelearningnavigator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.roadmap-sh.bootstrap")
public record RoadmapShBootstrapProperties(
        boolean enabled,
        boolean catalogSync,
        boolean roadmapSync
) {

    public RoadmapShBootstrapProperties {
        if (!catalogSync && !roadmapSync) {
            roadmapSync = true;
        }
    }
}
