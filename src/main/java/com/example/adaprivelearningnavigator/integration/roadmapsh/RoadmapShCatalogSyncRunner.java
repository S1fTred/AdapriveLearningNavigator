package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShCatalogSyncProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoadmapShCatalogSyncRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RoadmapShCatalogSyncRunner.class);

    private final RoadmapShCatalogSyncService syncService;
    private final RoadmapShCatalogSyncProperties properties;

    public RoadmapShCatalogSyncRunner(RoadmapShCatalogSyncService syncService,
                                      RoadmapShCatalogSyncProperties properties) {
        this.syncService = syncService;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.enabled()) {
            return;
        }

        RoadmapShCatalogSyncResult result = syncService.syncCatalog();
        log.info(
                "roadmap.sh catalog sync finished: total={}, created={}, updated={}, unchanged={}",
                result.totalEntries(),
                result.created(),
                result.updated(),
                result.unchanged()
        );
    }
}
