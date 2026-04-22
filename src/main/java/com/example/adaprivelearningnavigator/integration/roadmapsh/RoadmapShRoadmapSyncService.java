package com.example.adaprivelearningnavigator.integration.roadmapsh;

import com.example.adaprivelearningnavigator.config.RoadmapShRoadmapSyncProperties;
import com.example.adaprivelearningnavigator.domain.compositeKeys.RoleTopicId;
import com.example.adaprivelearningnavigator.domain.compositeKeys.TopicResourceId;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RoadmapShRoadmapSyncService {

    private final RoadmapShRoadmapSource roadmapSource;
    private final RoleGoalRepository roleGoalRepository;
    private final TopicRepository topicRepository;
    private final RoleTopicRepository roleTopicRepository;
    private final TopicPrereqRepository topicPrereqRepository;
    private final ResourceRepository resourceRepository;
    private final TopicResourceRepository topicResourceRepository;
    private final RoadmapShRoadmapSyncProperties properties;
    private final JdbcTemplate jdbcTemplate;

    public RoadmapShRoadmapSyncService(RoadmapShRoadmapSource roadmapSource,
                                       RoleGoalRepository roleGoalRepository,
                                       TopicRepository topicRepository,
                                       RoleTopicRepository roleTopicRepository,
                                       TopicPrereqRepository topicPrereqRepository,
                                       ResourceRepository resourceRepository,
                                       TopicResourceRepository topicResourceRepository,
                                       RoadmapShRoadmapSyncProperties properties,
                                       JdbcTemplate jdbcTemplate) {
        this.roadmapSource = roadmapSource;
        this.roleGoalRepository = roleGoalRepository;
        this.topicRepository = topicRepository;
        this.roleTopicRepository = roleTopicRepository;
        this.topicPrereqRepository = topicPrereqRepository;
        this.resourceRepository = resourceRepository;
        this.topicResourceRepository = topicResourceRepository;
        this.properties = properties;
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoadmapShRoadmapSyncSummary syncRoadmaps() {
        alignIdentityColumns();

        int roadmapsProcessed = 0;
        int rolesCreated = 0;
        int rolesUpdated = 0;
        int topicsCreated = 0;
        int topicsUpdated = 0;
        int roleTopicsCreated = 0;
        int roleTopicsUpdated = 0;
        int prereqsCreated = 0;
        int prereqsUpdated = 0;
        int resourcesCreated = 0;
        int resourcesUpdated = 0;
        int topicResourcesCreated = 0;
        int topicResourcesUpdated = 0;

        for (RoadmapShRoadmapManifest manifest : roadmapSource.loadRoadmaps()) {
            roadmapsProcessed++;

            RoleGoal role = roleGoalRepository.findByCode(manifest.roleCode()).orElse(null);
            if (role == null) {
                role = roleGoalRepository.save(RoleGoal.builder()
                        .code(manifest.roleCode())
                        .name(manifest.roleName())
                        .description(manifest.roleDescription())
                        .status(manifest.roleStatus() != null ? manifest.roleStatus() : EntityStatus.ACTIVE)
                        .build());
                rolesCreated++;
            } else if (properties.updateExisting()) {
                boolean dirty = false;
                EntityStatus targetStatus = manifest.roleStatus() != null ? manifest.roleStatus() : EntityStatus.ACTIVE;
                if (!Objects.equals(role.getName(), manifest.roleName())) {
                    role.setName(manifest.roleName());
                    dirty = true;
                }
                if (!Objects.equals(role.getDescription(), manifest.roleDescription())) {
                    role.setDescription(manifest.roleDescription());
                    dirty = true;
                }
                if (role.getStatus() != targetStatus) {
                    role.setStatus(targetStatus);
                    dirty = true;
                }
                if (dirty) {
                    role = roleGoalRepository.save(role);
                    rolesUpdated++;
                }
            }

            Map<String, Topic> topicsByCode = new HashMap<>();
            for (RoadmapShRoadmapTopicManifest topicManifest : safeTopics(manifest)) {
                Topic topic = topicRepository.findByCode(topicManifest.code()).orElse(null);
                if (topic == null) {
                    topic = topicRepository.save(Topic.builder()
                            .code(topicManifest.code())
                            .title(topicManifest.title())
                            .description(topicManifest.description())
                            .level(topicManifest.level())
                            .core(topicManifest.core())
                            .estimatedHours(topicManifest.estimatedHours())
                            .status(topicManifest.status() != null ? topicManifest.status() : EntityStatus.ACTIVE)
                            .build());
                    topicsCreated++;
                } else if (properties.updateExisting()) {
                    boolean dirty = false;
                    if (!Objects.equals(topic.getTitle(), topicManifest.title())) {
                        topic.setTitle(topicManifest.title());
                        dirty = true;
                    }
                    if (!Objects.equals(topic.getDescription(), topicManifest.description())) {
                        topic.setDescription(topicManifest.description());
                        dirty = true;
                    }
                    if (topic.getLevel() != topicManifest.level()) {
                        topic.setLevel(topicManifest.level());
                        dirty = true;
                    }
                    if (topic.isCore() != topicManifest.core()) {
                        topic.setCore(topicManifest.core());
                        dirty = true;
                    }
                    if (!Objects.equals(topic.getEstimatedHours(), topicManifest.estimatedHours())) {
                        topic.setEstimatedHours(topicManifest.estimatedHours());
                        dirty = true;
                    }
                    EntityStatus targetStatus = topicManifest.status() != null ? topicManifest.status() : EntityStatus.ACTIVE;
                    if (topic.getStatus() != targetStatus) {
                        topic.setStatus(targetStatus);
                        dirty = true;
                    }
                    if (dirty) {
                        topic = topicRepository.save(topic);
                        topicsUpdated++;
                    }
                }

                topicsByCode.put(topicManifest.code(), topic);

                RoleTopic roleTopic = roleTopicRepository.findByRole_IdAndTopic_Id(role.getId(), topic.getId()).orElse(null);
                if (roleTopic == null) {
                    roleTopicRepository.save(RoleTopic.builder()
                            .id(new RoleTopicId(role.getId(), topic.getId()))
                            .role(role)
                            .topic(topic)
                            .priority(topicManifest.priority())
                            .required(topicManifest.required())
                            .build());
                    roleTopicsCreated++;
                } else if (properties.updateExisting()) {
                    boolean dirty = false;
                    if (!Objects.equals(roleTopic.getPriority(), topicManifest.priority())) {
                        roleTopic.setPriority(topicManifest.priority());
                        dirty = true;
                    }
                    if (roleTopic.isRequired() != topicManifest.required()) {
                        roleTopic.setRequired(topicManifest.required());
                        dirty = true;
                    }
                    if (dirty) {
                        roleTopicRepository.save(roleTopic);
                        roleTopicsUpdated++;
                    }
                }
            }

            for (RoadmapShRoadmapTopicManifest topicManifest : safeTopics(manifest)) {
                Topic nextTopic = topicsByCode.get(topicManifest.code());

                for (RoadmapShRoadmapPrereqManifest prereqManifest : safePrereqs(topicManifest)) {
                    Topic prereqTopic = topicsByCode.get(prereqManifest.topicCode());
                    if (prereqTopic == null) {
                        throw new IllegalStateException("Unknown prereq topic code in roadmap manifest: " + prereqManifest.topicCode());
                    }

                    TopicPrereq prereq = topicPrereqRepository.findByPrereqTopic_IdAndNextTopic_Id(prereqTopic.getId(), nextTopic.getId())
                            .orElse(null);
                    PrereqRelationType relationType = prereqManifest.relationType() != null
                            ? prereqManifest.relationType()
                            : PrereqRelationType.REQUIRED;
                    if (prereq == null) {
                        topicPrereqRepository.save(TopicPrereq.builder()
                                .prereqTopic(prereqTopic)
                                .nextTopic(nextTopic)
                                .relationType(relationType)
                                .build());
                        prereqsCreated++;
                    } else if (properties.updateExisting() && prereq.getRelationType() != relationType) {
                        prereq.setRelationType(relationType);
                        topicPrereqRepository.save(prereq);
                        prereqsUpdated++;
                    }
                }

                for (RoadmapShRoadmapResourceManifest resourceManifest : safeResources(topicManifest)) {
                    Resource resource = resourceRepository.findByUrl(resourceManifest.url()).orElse(null);
                    if (resource == null) {
                        resource = resourceRepository.save(Resource.builder()
                                .title(resourceManifest.title())
                                .url(resourceManifest.url())
                                .type(resourceManifest.type())
                                .language(resourceManifest.language())
                                .durationMinutes(resourceManifest.durationMinutes())
                                .provider(resourceManifest.provider())
                                .difficulty(resourceManifest.difficulty())
                                .status(resourceManifest.status() != null ? resourceManifest.status() : EntityStatus.ACTIVE)
                                .build());
                        resourcesCreated++;
                    } else if (properties.updateExisting()) {
                        boolean dirty = false;
                        if (!Objects.equals(resource.getTitle(), resourceManifest.title())) {
                            resource.setTitle(resourceManifest.title());
                            dirty = true;
                        }
                        if (resource.getType() != resourceManifest.type()) {
                            resource.setType(resourceManifest.type());
                            dirty = true;
                        }
                        if (!Objects.equals(resource.getLanguage(), resourceManifest.language())) {
                            resource.setLanguage(resourceManifest.language());
                            dirty = true;
                        }
                        if (!Objects.equals(resource.getDurationMinutes(), resourceManifest.durationMinutes())) {
                            resource.setDurationMinutes(resourceManifest.durationMinutes());
                            dirty = true;
                        }
                        if (!Objects.equals(resource.getProvider(), resourceManifest.provider())) {
                            resource.setProvider(resourceManifest.provider());
                            dirty = true;
                        }
                        if (!Objects.equals(resource.getDifficulty(), resourceManifest.difficulty())) {
                            resource.setDifficulty(resourceManifest.difficulty());
                            dirty = true;
                        }
                        EntityStatus targetStatus = resourceManifest.status() != null ? resourceManifest.status() : EntityStatus.ACTIVE;
                        if (resource.getStatus() != targetStatus) {
                            resource.setStatus(targetStatus);
                            dirty = true;
                        }
                        if (dirty) {
                            resource = resourceRepository.save(resource);
                            resourcesUpdated++;
                        }
                    }

                    TopicResource topicResource = topicResourceRepository.findByTopic_IdAndResource_Id(nextTopic.getId(), resource.getId())
                            .orElse(null);
                    if (topicResource == null) {
                        topicResourceRepository.save(TopicResource.builder()
                                .id(new TopicResourceId(nextTopic.getId(), resource.getId()))
                                .topic(nextTopic)
                                .resource(resource)
                                .rank(resourceManifest.rank())
                                .build());
                        topicResourcesCreated++;
                    } else if (properties.updateExisting() && !Objects.equals(topicResource.getRank(), resourceManifest.rank())) {
                        topicResource.setRank(resourceManifest.rank());
                        topicResourceRepository.save(topicResource);
                        topicResourcesUpdated++;
                    }
                }
            }
        }

        return new RoadmapShRoadmapSyncSummary(
                roadmapsProcessed,
                rolesCreated,
                rolesUpdated,
                topicsCreated,
                topicsUpdated,
                roleTopicsCreated,
                roleTopicsUpdated,
                prereqsCreated,
                prereqsUpdated,
                resourcesCreated,
                resourcesUpdated,
                topicResourcesCreated,
                topicResourcesUpdated
        );
    }

    private List<RoadmapShRoadmapTopicManifest> safeTopics(RoadmapShRoadmapManifest manifest) {
        return manifest.topics() == null ? List.of() : manifest.topics();
    }

    private List<RoadmapShRoadmapPrereqManifest> safePrereqs(RoadmapShRoadmapTopicManifest topic) {
        return topic.prereqs() == null ? List.of() : topic.prereqs();
    }

    private List<RoadmapShRoadmapResourceManifest> safeResources(RoadmapShRoadmapTopicManifest topic) {
        return topic.resources() == null ? List.of() : topic.resources();
    }

    private void alignIdentityColumns() {
        alignIdentity("role_goals", "role_id");
        alignIdentity("topics", "topic_id");
        alignIdentity("topic_prereqs", "topic_prereq_id");
        alignIdentity("resources", "resource_id");
    }

    private void alignIdentity(String tableName, String columnName) {
        Long nextValue = jdbcTemplate.queryForObject(
                "select coalesce(max(" + columnName + "), 0) + 1 from " + tableName,
                Long.class
        );
        if (nextValue == null || nextValue < 1) {
            nextValue = 1L;
        }

        jdbcTemplate.execute(
                "alter table " + tableName + " alter column " + columnName + " restart with " + nextValue
        );
    }
}
