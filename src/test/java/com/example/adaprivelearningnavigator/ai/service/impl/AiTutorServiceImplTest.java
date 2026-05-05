package com.example.adaprivelearningnavigator.ai.service.impl;

import com.example.adaprivelearningnavigator.ai.dto.AiTutorRequest;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleGoal;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.RoleTopic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.TopicPrereq;
import com.example.adaprivelearningnavigator.repo.RoleGoalRepository;
import com.example.adaprivelearningnavigator.repo.RoleTopicRepository;
import com.example.adaprivelearningnavigator.repo.TopicPrereqRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiTutorServiceImplTest {

    @Mock
    private RoleGoalRepository roleGoalRepository;

    @Mock
    private RoleTopicRepository roleTopicRepository;

    @Mock
    private TopicPrereqRepository topicPrereqRepository;

    @Test
    void shouldSendChatHistoryToAiPrompt() {
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        ChatClient chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        ArgumentCaptor<String> userPromptCaptor = ArgumentCaptor.forClass(String.class);
        when(builder.build()).thenReturn(chatClient);
        when(chatClient.prompt().system(anyString()).user(userPromptCaptor.capture()).call().content())
                .thenReturn("Массив хранит элементы по индексам.");

        RoleGoal role = RoleGoal.builder()
                .id(10L)
                .code("javascript")
                .name("JavaScript")
                .build();
        Topic topic = Topic.builder()
                .id(20L)
                .code("JS_ARRAYS")
                .title("Arrays")
                .description("Arrays topic")
                .estimatedHours(BigDecimal.valueOf(4))
                .build();
        RoleTopic roleTopic = RoleTopic.builder()
                .role(role)
                .topic(topic)
                .priority(1)
                .required(true)
                .build();

        when(roleGoalRepository.findById(10L)).thenReturn(Optional.of(role));
        when(roleTopicRepository.findAllByRole_IdOrderByPriorityAsc(10L)).thenReturn(List.of(roleTopic));
        when(topicPrereqRepository.findAll()).thenReturn(List.<TopicPrereq>of());

        AiTutorServiceImpl service = new AiTutorServiceImpl(
                builder,
                roleGoalRepository,
                roleTopicRepository,
                topicPrereqRepository
        );

        var response = service.ask(
                10L,
                20L,
                new AiTutorRequest(
                        "Покажи короткий пример.",
                        List.of(
                                new AiTutorRequest.AiTutorMessage("user", "Что такое массив?"),
                                new AiTutorRequest.AiTutorMessage("assistant", "Массив хранит набор значений.")
                        )
                )
        );

        assertThat(response.answer()).isEqualTo("Массив хранит элементы по индексам.");
        assertThat(userPromptCaptor.getValue())
                .contains("Предыдущий диалог:")
                .contains("Пользователь: Что такое массив?")
                .contains("AI Tutor: Массив хранит набор значений.")
                .contains("Текущий вопрос пользователя:")
                .contains("Покажи короткий пример.");
    }
}
