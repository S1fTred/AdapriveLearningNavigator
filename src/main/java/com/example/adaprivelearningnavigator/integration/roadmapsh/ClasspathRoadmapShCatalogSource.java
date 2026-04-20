package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShCatalogSyncProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ClasspathRoadmapShCatalogSource implements RoadmapShCatalogSource {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final RoadmapShCatalogSyncProperties properties;

    public ClasspathRoadmapShCatalogSource(ObjectMapper objectMapper,
                                           ResourceLoader resourceLoader,
                                           RoadmapShCatalogSyncProperties properties) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.properties = properties;
    }

    @Override
    public List<RoadmapShCatalogEntry> loadCatalog() {
        Resource resource = resourceLoader.getResource(properties.catalogLocation());
        if (!resource.exists()) {
            throw new IllegalStateException("roadmap.sh catalog resource not found: " + properties.catalogLocation());
        }

        try (InputStream inputStream = resource.getInputStream()) {
            RoadmapShCatalogManifest manifest = objectMapper.readValue(inputStream, RoadmapShCatalogManifest.class);
            List<RoadmapShCatalogEntry> entries = manifest.entries() == null ? List.of() : manifest.entries();
            validate(entries);
            return entries;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read roadmap.sh catalog resource: " + properties.catalogLocation(), exception);
        }
    }

    private void validate(List<RoadmapShCatalogEntry> entries) {
        Set<String> seenCodes = new HashSet<>();
        for (RoadmapShCatalogEntry entry : entries) {
            if (entry == null) {
                throw new IllegalStateException("roadmap.sh catalog contains a null entry");
            }
            if (isBlank(entry.code())) {
                throw new IllegalStateException("roadmap.sh catalog contains an entry without code");
            }
            if (isBlank(entry.name())) {
                throw new IllegalStateException("roadmap.sh catalog contains an entry without name: " + entry.code());
            }
            if (!seenCodes.add(entry.code())) {
                throw new IllegalStateException("roadmap.sh catalog contains duplicated code: " + entry.code());
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
