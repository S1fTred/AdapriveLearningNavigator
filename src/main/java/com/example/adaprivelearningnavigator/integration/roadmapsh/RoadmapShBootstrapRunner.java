package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShBootstrapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.roadmap-sh.bootstrap", name = "enabled", havingValue = "true")
public class RoadmapShBootstrapRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RoadmapShBootstrapRunner.class);

    private final RoadmapShBootstrapProperties properties;
    private final RoadmapShCatalogSyncService catalogSyncService;
    private final RoadmapShRoadmapSyncService roadmapSyncService;
    private final ConfigurableApplicationContext applicationContext;

    public RoadmapShBootstrapRunner(RoadmapShBootstrapProperties properties,
                                    RoadmapShCatalogSyncService catalogSyncService,
                                    RoadmapShRoadmapSyncService roadmapSyncService,
                                    ConfigurableApplicationContext applicationContext) {
        this.properties = properties;
        this.catalogSyncService = catalogSyncService;
        this.roadmapSyncService = roadmapSyncService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        long startedAt = System.nanoTime();
        log.info("roadmap.sh KB bootstrap started");

        if (properties.catalogSync()) {
            RoadmapShCatalogSyncResult result = catalogSyncService.syncCatalog();
            log.info(
                    "roadmap.sh bootstrap catalog phase finished: total={}, created={}, updated={}, unchanged={}",
                    result.totalEntries(),
                    result.created(),
                    result.updated(),
                    result.unchanged()
            );
        }

        if (properties.roadmapSync()) {
            RoadmapShRoadmapSyncSummary summary = roadmapSyncService.syncRoadmaps();
            log.info(
                    "roadmap.sh bootstrap content phase finished: roadmaps={}, rolesCreated={}, rolesUpdated={}, topicsCreated={}, topicsUpdated={}, roleTopicsCreated={}, roleTopicsUpdated={}, prereqsCreated={}, prereqsUpdated={}, resourcesCreated={}, resourcesUpdated={}, topicResourcesCreated={}, topicResourcesUpdated={}",
                    summary.roadmapsProcessed(),
                    summary.rolesCreated(),
                    summary.rolesUpdated(),
                    summary.topicsCreated(),
                    summary.topicsUpdated(),
                    summary.roleTopicsCreated(),
                    summary.roleTopicsUpdated(),
                    summary.prereqsCreated(),
                    summary.prereqsUpdated(),
                    summary.resourcesCreated(),
                    summary.resourcesUpdated(),
                    summary.topicResourcesCreated(),
                    summary.topicResourcesUpdated()
            );
        }

        long durationMs = (System.nanoTime() - startedAt) / 1_000_000L;
        log.info("roadmap.sh KB bootstrap finished in {} ms", durationMs);

        SpringApplication.exit(applicationContext, () -> 0);
    }
}
