package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Resource;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicResource;
import com.example.adaprivelearningnavigator.repo.QuizRepository;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicPrereqRepository;
import com.example.adaprivelearningnavigator.repo.TopicResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceImplTest {

    @Mock
    private RoleGoalRepository roleGoalRepository;

    @Mock
    private RoleTopicRepository roleTopicRepository;

    @Mock
    private TopicPrereqRepository topicPrereqRepository;

    @Mock
    private TopicResourceRepository topicResourceRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private RoadmapServiceImpl roadmapService;

    @Test
    void shouldExposeOnlyMvpRoadmapsInCatalog() {
        RoleGoal visibleRole = RoleGoal.builder()
                .id(101L)
                .code("frontend")
                .name("Frontend Developer")
                .status(EntityStatus.ACTIVE)
                .build();
        RoleGoal hiddenLegacyRole = RoleGoal.builder()
                .id(102L)
                .code("java-backend")
                .name("Java Backend Developer")
                .status(EntityStatus.ACTIVE)
                .build();
        RoleGoal hiddenExtraSkillRole = RoleGoal.builder()
                .id(103L)
                .code("scala")
                .name("Scala")
                .status(EntityStatus.ACTIVE)
                .build();
        Topic topic = Topic.builder()
                .id(501L)
                .code("HTML")
                .title("HTML")
                .estimatedHours(BigDecimal.valueOf(4))
                .build();
        RoleTopic roleTopic = RoleTopic.builder()
                .role(visibleRole)
                .topic(topic)
                .priority(1)
                .required(true)
                .build();

        when(roleGoalRepository.findAllByStatusOrderByNameAsc(EntityStatus.ACTIVE))
                .thenReturn(List.of(visibleRole, hiddenLegacyRole, hiddenExtraSkillRole));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(101L)).thenReturn(List.of(roleTopic));

        var catalog = roadmapService.getRoadmaps(0, 12);

        assertThat(catalog.items()).hasSize(1);
        assertThat(catalog.total()).isEqualTo(1);
        assertThat(catalog.items().get(0).code()).isEqualTo("frontend");
    }

    @Test
    void shouldHideRoadmapShSourceMarkersInCatalogAndDetailDescriptions() {
        RoleGoal role = RoleGoal.builder()
                .id(101L)
                .code("frontend")
                .name("Frontend Developer")
                .description("Дорожная карта по направлению «Frontend Developer» из каталога roadmap.sh. Темы и материалы адаптированы для русскоязычного интерфейса сервиса.")
                .status(EntityStatus.ACTIVE)
                .build();

        Topic topic = Topic.builder()
                .id(501L)
                .code("HTML")
                .title("HTML")
                .description("Подтема roadmap по направлению «Frontend Developer»: HTML. Добавлено из roadmap.sh.")
                .estimatedHours(BigDecimal.valueOf(4))
                .build();

        RoleTopic roleTopic = RoleTopic.builder()
                .role(role)
                .topic(topic)
                .priority(1)
                .required(true)
                .build();

        when(roleGoalRepository.findAllByStatusOrderByNameAsc(EntityStatus.ACTIVE)).thenReturn(List.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(101L)).thenReturn(List.of(roleTopic));
        when(roleGoalRepository.findById(101L)).thenReturn(Optional.of(role));
        when(topicPrereqRepository.findAll()).thenReturn(List.of());
        when(topicResourceRepository.findAllByTopic_IdOrderByRankAsc(501L)).thenReturn(List.of());
        when(quizRepository.findByTopic_Id(501L)).thenReturn(Optional.empty());

        var catalog = roadmapService.getRoadmaps(0, 12);
        var detail = roadmapService.getRoadmap(101L);
        var topicDetail = roadmapService.getRoadmapTopic(101L, 501L);

        assertThat(catalog.items()).hasSize(1);
        assertThat(catalog.items().get(0).description()).doesNotContain("roadmap.sh");
        assertThat(catalog.items().get(0).description()).doesNotContain("адаптированы для русскоязычного интерфейса сервиса");
        assertThat(detail.description()).doesNotContain("roadmap.sh");
        assertThat(topicDetail.description()).doesNotContain("roadmap.sh");
        assertThat(topicDetail.description()).isEqualTo("Подтема направления «Frontend-разработчик»: HTML.");
    }

    @Test
    void shouldHideRoadmapShProviderInTopicResources() {
        RoleGoal role = RoleGoal.builder()
                .id(102L)
                .code("devops")
                .name("DevOps")
                .description("DevOps roadmap")
                .status(EntityStatus.ACTIVE)
                .build();

        Topic topic = Topic.builder()
                .id(601L)
                .code("DOCKER")
                .title("Docker")
                .estimatedHours(BigDecimal.valueOf(5))
                .build();

        RoleTopic roleTopic = RoleTopic.builder()
                .role(role)
                .topic(topic)
                .priority(1)
                .required(true)
                .build();

        Resource resource = Resource.builder()
                .id(701L)
                .title("Docker roadmap")
                .url("https://roadmap.sh/docker")
                .type(ResourceType.ARTICLE)
                .provider("roadmap.sh")
                .build();

        TopicResource topicResource = TopicResource.builder()
                .topic(topic)
                .resource(resource)
                .rank(1)
                .build();

        when(roleGoalRepository.findById(102L)).thenReturn(Optional.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(102L)).thenReturn(List.of(roleTopic));
        when(topicPrereqRepository.findAll()).thenReturn(List.of());
        when(topicResourceRepository.findAllByTopic_IdOrderByRankAsc(601L)).thenReturn(List.of(topicResource));
        when(quizRepository.findByTopic_Id(601L)).thenReturn(Optional.empty());

        var topicDetail = roadmapService.getRoadmapTopic(102L, 601L);

        assertThat(topicDetail.resources()).hasSize(1);
        assertThat(topicDetail.resources().get(0).provider()).isNull();
    }
}
