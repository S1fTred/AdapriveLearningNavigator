package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShCatalogSyncProperties;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClasspathRoadmapShCatalogSourceTest {

    @Test
    void shouldLoadBundledRoadmapShCatalog() {
        ClasspathRoadmapShCatalogSource source = new ClasspathRoadmapShCatalogSource(
                JsonMapper.builder().findAndAddModules().build(),
                new DefaultResourceLoader(),
                new RoadmapShCatalogSyncProperties(
                        false,
                        "classpath:roadmap-sh/catalog.json",
                        null,
                        true
                )
        );

        List<RoadmapShCatalogEntry> entries = source.loadCatalog();

        assertTrue(entries.size() >= 20);
        assertTrue(entries.stream().anyMatch(entry -> "frontend".equals(entry.code())));
        assertTrue(entries.stream().anyMatch(entry -> "java".equals(entry.code())));
        assertTrue(entries.stream().anyMatch(entry -> "game-developer".equals(entry.code())));
    }
}
