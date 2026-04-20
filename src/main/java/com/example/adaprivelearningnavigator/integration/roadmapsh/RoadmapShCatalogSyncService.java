package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShCatalogSyncProperties;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class RoadmapShCatalogSyncService {

    private final RoadmapShCatalogSource catalogSource;
    private final RoleGoalRepository roleGoalRepository;
    private final RoadmapShCatalogSyncProperties properties;

    public RoadmapShCatalogSyncService(RoadmapShCatalogSource catalogSource,
                                       RoleGoalRepository roleGoalRepository,
                                       RoadmapShCatalogSyncProperties properties) {
        this.catalogSource = catalogSource;
        this.roleGoalRepository = roleGoalRepository;
        this.properties = properties;
    }

    public RoadmapShCatalogSyncResult syncCatalog() {
        List<RoadmapShCatalogEntry> entries = catalogSource.loadCatalog();
        int created = 0;
        int updated = 0;
        int unchanged = 0;

        for (RoadmapShCatalogEntry entry : entries) {
            RoleGoal roleGoal = roleGoalRepository.findByCode(entry.code()).orElse(null);
            if (roleGoal == null) {
                roleGoalRepository.save(RoleGoal.builder()
                        .code(entry.code())
                        .name(entry.name())
                        .description(entry.description())
                        .status(properties.defaultStatus())
                        .build());
                created++;
                continue;
            }

            if (!properties.updateExisting()) {
                unchanged++;
                continue;
            }

            boolean dirty = false;
            if (!Objects.equals(roleGoal.getName(), entry.name())) {
                roleGoal.setName(entry.name());
                dirty = true;
            }
            if (!Objects.equals(roleGoal.getDescription(), entry.description())) {
                roleGoal.setDescription(entry.description());
                dirty = true;
            }

            if (dirty) {
                roleGoalRepository.save(roleGoal);
                updated++;
            } else {
                unchanged++;
            }
        }

        return new RoadmapShCatalogSyncResult(entries.size(), created, updated, unchanged);
    }
}
