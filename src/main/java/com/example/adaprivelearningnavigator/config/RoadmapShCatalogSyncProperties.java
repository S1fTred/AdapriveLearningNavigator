package com.example.adaprivelearningnavigator.config;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.roadmap-sh.catalog-sync")
public record RoadmapShCatalogSyncProperties(
        boolean enabled,
        String catalogLocation,
        EntityStatus defaultStatus,
        boolean updateExisting
) {
    public RoadmapShCatalogSyncProperties {
        if (catalogLocation == null || catalogLocation.isBlank()) {
            catalogLocation = "classpath:roadmap-sh/catalog.json";
        }
        if (defaultStatus == null) {
            defaultStatus = EntityStatus.DRAFT;
        }
    }
}
