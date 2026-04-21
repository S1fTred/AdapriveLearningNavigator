package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShRoadmapSyncProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ClasspathRoadmapShRoadmapSource implements RoadmapShRoadmapSource {

    private final ObjectMapper objectMapper;
    private final PathMatchingResourcePatternResolver resourcePatternResolver;
    private final RoadmapShRoadmapSyncProperties properties;

    public ClasspathRoadmapShRoadmapSource(ObjectMapper objectMapper,
                                           ResourceLoader resourceLoader,
                                           RoadmapShRoadmapSyncProperties properties) {
        this.objectMapper = objectMapper;
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
        this.properties = properties;
    }

    @Override
    public List<RoadmapShRoadmapManifest> loadRoadmaps() {
        try {
            Resource[] resources = resolveResources(properties.manifestPattern());
            Map<String, RoadmapShRoadmapManifest> manifestsByRoleCode = new LinkedHashMap<>();
            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    RoadmapShRoadmapManifest manifest = objectMapper.readValue(inputStream, RoadmapShRoadmapManifest.class);
                    if (manifest != null && !isBlank(manifest.roleCode())) {
                        manifestsByRoleCode.putIfAbsent(manifest.roleCode(), manifest);
                    }
                }
            }

            List<RoadmapShRoadmapManifest> manifests = new ArrayList<>(manifestsByRoleCode.values());
            validate(manifests);
            return manifests;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read roadmap.sh roadmap manifests: " + properties.manifestPattern(), exception);
        }
    }

    private Resource[] resolveResources(String patternValue) throws IOException {
        String[] patterns = patternValue.split("[,;\\r\\n]+");
        Map<String, Resource> uniqueResources = new LinkedHashMap<>();

        for (String rawPattern : patterns) {
            String pattern = rawPattern == null ? "" : rawPattern.trim();
            if (pattern.isEmpty()) {
                continue;
            }

            for (Resource resource : resourcePatternResolver.getResources(pattern)) {
                uniqueResources.putIfAbsent(resource.getURL().toExternalForm(), resource);
            }
        }

        return uniqueResources.values().toArray(Resource[]::new);
    }

    private void validate(List<RoadmapShRoadmapManifest> manifests) {
        Set<String> roleCodes = new HashSet<>();
        for (RoadmapShRoadmapManifest manifest : manifests) {
            if (manifest == null) {
                throw new IllegalStateException("roadmap.sh roadmap manifests contain a null entry");
            }
            if (isBlank(manifest.roleCode())) {
                throw new IllegalStateException("roadmap.sh roadmap manifest contains empty roleCode");
            }
            if (isBlank(manifest.roleName())) {
                throw new IllegalStateException("roadmap.sh roadmap manifest contains empty roleName: " + manifest.roleCode());
            }
            if (!roleCodes.add(manifest.roleCode())) {
                throw new IllegalStateException("roadmap.sh roadmap manifests contain duplicated roleCode: " + manifest.roleCode());
            }
            validateTopics(manifest);
        }
    }

    private void validateTopics(RoadmapShRoadmapManifest manifest) {
        Set<String> topicCodes = new HashSet<>();
        for (RoadmapShRoadmapTopicManifest topic : manifest.topics() == null ? List.<RoadmapShRoadmapTopicManifest>of() : manifest.topics()) {
            if (isBlank(topic.code())) {
                throw new IllegalStateException("roadmap.sh roadmap manifest contains topic without code: " + manifest.roleCode());
            }
            if (isBlank(topic.title())) {
                throw new IllegalStateException("roadmap.sh roadmap manifest contains topic without title: " + topic.code());
            }
            if (!topicCodes.add(topic.code())) {
                throw new IllegalStateException("roadmap.sh roadmap manifest contains duplicated topic code: " + topic.code());
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
