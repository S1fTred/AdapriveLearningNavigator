package com.example.adaprivelearningnavigator.integration.roadmapsh;

public record RoadmapShRoadmapSyncSummary(
        int roadmapsProcessed,
        int rolesCreated,
        int rolesUpdated,
        int topicsCreated,
        int topicsUpdated,
        int roleTopicsCreated,
        int roleTopicsUpdated,
        int prereqsCreated,
        int prereqsUpdated,
        int resourcesCreated,
        int resourcesUpdated,
        int topicResourcesCreated,
        int topicResourcesUpdated
) {
}
