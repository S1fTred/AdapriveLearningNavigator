package com.example.adaprivelearningnavigator.integration.roadmapsh;

import java.util.List;

public record RoadmapShCatalogManifest(
        String source,
        String sourceUrl,
        List<RoadmapShCatalogEntry> entries
) {
}
