package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShRoadmapSyncProperties;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Resource;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicResource;
import com.example.adaprivelearningnavigator.repo.ResourceRepository;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicPrereqRepository;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadmapShRoadmapSyncServiceTest {

    @Mock private RoadmapShRoadmapSource roadmapSource;
    @Mock private RoleGoalRepository roleGoalRepository;
    @Mock private TopicRepository topicRepository;
    @Mock private RoleTopicRepository roleTopicRepository;
    @Mock private TopicPrereqRepository topicPrereqRepository;
    @Mock private ResourceRepository resourceRepository;
    @Mock private TopicResourceRepository topicResourceRepository;
    @Mock private JdbcTemplate jdbcTemplate;

    @Test
    void shouldCreateRoadmapTopicsPrereqsAndResources() {
        RoadmapShRoadmapManifest manifest = new RoadmapShRoadmapManifest(
                "roadmap.sh",
                "https://roadmap.sh/game-developer",
                "game-developer",
                "Game Developer",
                "Game dev roadmap",
                com.example.adaprivelearningnavigator.domain.enums.EntityStatus.ACTIVE,
                List.of(
                        new RoadmapShRoadmapTopicManifest(
                                "CSHARP_BASICS",
                                "C# Basics",
                                "Basics",
                                com.example.adaprivelearningnavigator.domain.enums.TopicLevel.BASIC,
                                true,
                                java.math.BigDecimal.valueOf(8),
                                com.example.adaprivelearningnavigator.domain.enums.EntityStatus.ACTIVE,
                                1,
                                true,
                                List.of(),
                                List.of(new RoadmapShRoadmapResourceManifest(
                                        "C# docs",
                                        "https://learn.microsoft.com/en-us/dotnet/csharp/",
                                        com.example.adaprivelearningnavigator.domain.enums.ResourceType.ARTICLE,
                                        "en",
                                        120,
                                        "Microsoft Learn",
                                        "BEGINNER",
                                        com.example.adaprivelearningnavigator.domain.enums.EntityStatus.ACTIVE,
                                        1
                                ))
                        ),
                        new RoadmapShRoadmapTopicManifest(
                                "UNITY_EDITOR_FUNDAMENTALS",
                                "Unity Editor Fundamentals",
                                "Editor",
                                com.example.adaprivelearningnavigator.domain.enums.TopicLevel.BASIC,
                                true,
                                java.math.BigDecimal.valueOf(8),
                                com.example.adaprivelearningnavigator.domain.enums.EntityStatus.ACTIVE,
                                2,
                                true,
                                List.of(new RoadmapShRoadmapPrereqManifest(
                                        "CSHARP_BASICS",
                                        com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType.REQUIRED
                                )),
                                List.of(new RoadmapShRoadmapResourceManifest(
                                        "Editor Essentials",
                                        "https://learn.unity.com/pathway/unity-essentials/unit/editor-essentials",
                                        com.example.adaprivelearningnavigator.domain.enums.ResourceType.INTERACTIVE,
                                        "en",
                                        120,
                                        "Unity Learn",
                                        "BEGINNER",
                                        com.example.adaprivelearningnavigator.domain.enums.EntityStatus.ACTIVE,
                                        1
                                ))
                        )
                )
        );

        when(roadmapSource.loadRoadmaps()).thenReturn(List.of(manifest));
        when(roleGoalRepository.findByCode("game-developer")).thenReturn(Optional.empty());
        when(topicRepository.findByCode("CSHARP_BASICS")).thenReturn(Optional.empty());
        when(topicRepository.findByCode("UNITY_EDITOR_FUNDAMENTALS")).thenReturn(Optional.empty());
        when(roleTopicRepository.findByRole_IdAndTopic_Id(any(), any())).thenReturn(Optional.empty());
        when(topicPrereqRepository.findByPrereqTopic_IdAndNextTopic_Id(any(), any())).thenReturn(Optional.empty());
        when(resourceRepository.findByUrl("https://learn.microsoft.com/en-us/dotnet/csharp/")).thenReturn(Optional.empty());
        when(resourceRepository.findByUrl("https://learn.unity.com/pathway/unity-essentials/unit/editor-essentials")).thenReturn(Optional.empty());
        when(topicResourceRepository.findByTopic_IdAndResource_Id(any(), any())).thenReturn(Optional.empty());
        when(jdbcTemplate.queryForObject(any(String.class), org.mockito.ArgumentMatchers.eq(Long.class))).thenReturn(1L);

        AtomicLong roleIds = new AtomicLong(100L);
        AtomicLong topicIds = new AtomicLong(200L);
        AtomicLong resourceIds = new AtomicLong(300L);

        when(roleGoalRepository.save(any(RoleGoal.class))).thenAnswer(invocation -> {
            RoleGoal role = invocation.getArgument(0);
            if (role.getId() == null) {
                role.setId(roleIds.getAndIncrement());
            }
            return role;
        });
        when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> {
            Topic topic = invocation.getArgument(0);
            if (topic.getId() == null) {
                topic.setId(topicIds.getAndIncrement());
            }
            return topic;
        });
        when(resourceRepository.save(any(Resource.class))).thenAnswer(invocation -> {
            Resource resource = invocation.getArgument(0);
            if (resource.getId() == null) {
                resource.setId(resourceIds.getAndIncrement());
            }
            return resource;
        });
        when(roleTopicRepository.save(any(RoleTopic.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(topicPrereqRepository.save(any(TopicPrereq.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(topicResourceRepository.save(any(TopicResource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoadmapShRoadmapSyncService service = new RoadmapShRoadmapSyncService(
                roadmapSource,
                roleGoalRepository,
                topicRepository,
                roleTopicRepository,
                topicPrereqRepository,
                resourceRepository,
                topicResourceRepository,
                new RoadmapShRoadmapSyncProperties(
                        false,
                        "classpath*:roadmap-sh/roadmaps/*.json",
                        true
                ),
                jdbcTemplate
        );

        RoadmapShRoadmapSyncSummary summary = service.syncRoadmaps();

        assertEquals(1, summary.roadmapsProcessed());
        assertEquals(1, summary.rolesCreated());
        assertEquals(2, summary.topicsCreated());
        assertEquals(2, summary.roleTopicsCreated());
        assertEquals(1, summary.prereqsCreated());
        assertEquals(2, summary.resourcesCreated());
        assertEquals(2, summary.topicResourcesCreated());
    }

    @Test
    void shouldPromoteExistingDraftRoleToActiveWhenManifestStatusIsMissing() {
        RoadmapShRoadmapManifest manifest = new RoadmapShRoadmapManifest(
                "roadmap.sh",
                "https://roadmap.sh/frontend",
                "frontend",
                "Frontend",
                "Frontend roadmap",
                null,
                List.of()
        );

        RoleGoal existingRole = RoleGoal.builder()
                .id(10L)
                .code("frontend")
                .name("Frontend")
                .description("Old description")
                .status(EntityStatus.DRAFT)
                .build();

        when(roadmapSource.loadRoadmaps()).thenReturn(List.of(manifest));
        when(roleGoalRepository.findByCode("frontend")).thenReturn(Optional.of(existingRole));
        when(roleGoalRepository.save(any(RoleGoal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jdbcTemplate.queryForObject(any(String.class), org.mockito.ArgumentMatchers.eq(Long.class))).thenReturn(1L);

        RoadmapShRoadmapSyncService service = new RoadmapShRoadmapSyncService(
                roadmapSource,
                roleGoalRepository,
                topicRepository,
                roleTopicRepository,
                topicPrereqRepository,
                resourceRepository,
                topicResourceRepository,
                new RoadmapShRoadmapSyncProperties(
                        false,
                        "classpath*:roadmap-sh/roadmaps/*.json",
                        true
                ),
                jdbcTemplate
        );

        RoadmapShRoadmapSyncSummary summary = service.syncRoadmaps();

        assertEquals(1, summary.roadmapsProcessed());
        assertEquals(0, summary.rolesCreated());
        assertEquals(1, summary.rolesUpdated());
        assertEquals(EntityStatus.ACTIVE, existingRole.getStatus());
    }
}
