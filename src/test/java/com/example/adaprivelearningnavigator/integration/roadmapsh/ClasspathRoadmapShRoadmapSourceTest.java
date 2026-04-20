package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShRoadmapSyncProperties;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClasspathRoadmapShRoadmapSourceTest {

    @Test
    void shouldLoadBundledRoadmapManifests() {
        ClasspathRoadmapShRoadmapSource source = new ClasspathRoadmapShRoadmapSource(
                JsonMapper.builder().findAndAddModules().build(),
                new DefaultResourceLoader(),
                new RoadmapShRoadmapSyncProperties(
                        false,
                        "classpath*:roadmap-sh/roadmaps/*.json",
                        true
                )
        );

        List<RoadmapShRoadmapManifest> manifests = source.loadRoadmaps();

        assertEquals(1, manifests.size());
        assertEquals("game-developer", manifests.get(0).roleCode());
        assertTrue(manifests.get(0).topics().size() >= 6);
    }
}
