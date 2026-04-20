package com.example.adaprivelearningnavigator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        RoadmapShCatalogSyncProperties.class,
        RoadmapShRoadmapSyncProperties.class
})
public class RoadmapShImportConfig {
}
