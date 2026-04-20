package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShCatalogSyncProperties;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadmapShCatalogSyncServiceTest {

    @Mock
    private RoadmapShCatalogSource catalogSource;

    @Mock
    private RoleGoalRepository roleGoalRepository;

    @Test
    void shouldCreateNewDraftRoadmapsAndUpdateExistingOnes() {
        RoadmapShCatalogEntry frontend = new RoadmapShCatalogEntry(
                "frontend",
                "Frontend",
                "Roadmap po frontend-razrabotke.",
                "web"
        );
        RoadmapShCatalogEntry backend = new RoadmapShCatalogEntry(
                "backend",
                "Backend",
                "Roadmap po backend-razrabotke.",
                "web"
        );

        RoleGoal existingBackend = RoleGoal.builder()
                .id(200L)
                .code("backend")
                .name("Old Backend")
                .description("Old description")
                .status(EntityStatus.ACTIVE)
                .build();

        when(catalogSource.loadCatalog()).thenReturn(List.of(frontend, backend));
        when(roleGoalRepository.findByCode("frontend")).thenReturn(Optional.empty());
        when(roleGoalRepository.findByCode("backend")).thenReturn(Optional.of(existingBackend));
        when(roleGoalRepository.save(any(RoleGoal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoadmapShCatalogSyncService service = new RoadmapShCatalogSyncService(
                catalogSource,
                roleGoalRepository,
                new RoadmapShCatalogSyncProperties(
                        false,
                        "classpath:roadmap-sh/catalog.json",
                        EntityStatus.DRAFT,
                        true
                )
        );

        RoadmapShCatalogSyncResult result = service.syncCatalog();

        assertEquals(2, result.totalEntries());
        assertEquals(1, result.created());
        assertEquals(1, result.updated());
        assertEquals(0, result.unchanged());

        ArgumentCaptor<RoleGoal> captor = ArgumentCaptor.forClass(RoleGoal.class);
        verify(roleGoalRepository, times(2)).save(captor.capture());

        List<RoleGoal> saved = captor.getAllValues();
        RoleGoal createdFrontend = saved.stream()
                .filter(item -> "frontend".equals(item.getCode()))
                .findFirst()
                .orElseThrow();
        RoleGoal updatedBackend = saved.stream()
                .filter(item -> "backend".equals(item.getCode()))
                .findFirst()
                .orElseThrow();

        assertNull(createdFrontend.getId());
        assertEquals(EntityStatus.DRAFT, createdFrontend.getStatus());
        assertEquals("Backend", updatedBackend.getName());
        assertEquals("Roadmap po backend-razrabotke.", updatedBackend.getDescription());
        assertEquals(EntityStatus.ACTIVE, updatedBackend.getStatus());
    }
}
