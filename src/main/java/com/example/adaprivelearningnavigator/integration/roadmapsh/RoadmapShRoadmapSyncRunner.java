package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShRoadmapSyncProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoadmapShRoadmapSyncRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RoadmapShRoadmapSyncRunner.class);

    private final RoadmapShRoadmapSyncService syncService;
    private final RoadmapShRoadmapSyncProperties properties;

    public RoadmapShRoadmapSyncRunner(RoadmapShRoadmapSyncService syncService,
                                      RoadmapShRoadmapSyncProperties properties) {
        this.syncService = syncService;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.enabled()) {
            return;
        }

        RoadmapShRoadmapSyncSummary summary = syncService.syncRoadmaps();
        log.info(
                "roadmap.sh content sync finished: roadmaps={}, rolesCreated={}, rolesUpdated={}, topicsCreated={}, topicsUpdated={}, roleTopicsCreated={}, roleTopicsUpdated={}, prereqsCreated={}, prereqsUpdated={}, resourcesCreated={}, resourcesUpdated={}, topicResourcesCreated={}, topicResourcesUpdated={}",
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
}
