package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShRoadmapSyncProperties;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClasspathRoadmapShRoadmapSourceTest {

    @Test
    void shouldLoadBundledRoadmapManifests() {
        ClasspathRoadmapShRoadmapSource source = new ClasspathRoadmapShRoadmapSource(
                JsonMapper.builder().findAndAddModules().build(),
                new DefaultResourceLoader(),
                new RoadmapShRoadmapSyncProperties(
                        false,
                        "file:./src/main/resources/roadmap-sh/roadmaps/*.json,classpath*:roadmap-sh/roadmaps/*.json",
                        true
                )
        );

        List<RoadmapShRoadmapManifest> manifests = source.loadRoadmaps();
        Map<String, Long> roleCodeCounts = manifests.stream()
                .collect(Collectors.groupingBy(RoadmapShRoadmapManifest::roleCode, Collectors.counting()));

        assertTrue(manifests.size() >= 80);
        assertEquals(1L, roleCodeCounts.get("ai-agents"));
        assertTrue(manifests.stream().anyMatch(manifest -> "game-developer".equals(manifest.roleCode())));
        assertTrue(manifests.stream().anyMatch(manifest -> "java-backend".equals(manifest.roleCode())));
        assertTrue(manifests.stream().anyMatch(manifest -> "java-mobile".equals(manifest.roleCode())));
        assertTrue(manifests.stream().anyMatch(manifest -> "backend".equals(manifest.roleCode())));
        assertTrue(manifests.stream().anyMatch(manifest -> "docker".equals(manifest.roleCode())));
        assertTrue(manifests.stream().anyMatch(manifest -> "html".equals(manifest.roleCode())));
        assertTrue(manifests.stream()
                .filter(manifest -> "game-developer".equals(manifest.roleCode()))
                .findFirst()
                .orElseThrow()
                .topics()
                .size() >= 6);
    }
}
