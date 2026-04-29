package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.knowledgeBase.Topic;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.Quiz;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizOption;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizQuestion;
import com.example.adaprivelearningnavigator.repo.QuizAttemptRepository;
import com.example.adaprivelearningnavigator.repo.QuizOptionRepository;
import com.example.adaprivelearningnavigator.repo.QuizQuestionRepository;
import com.example.adaprivelearningnavigator.repo.QuizRepository;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizQuestionRepository questionRepository;

    @Mock
    private QuizOptionRepository optionRepository;

    @Mock
    private QuizAttemptRepository attemptRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    @Test
    void shouldCreateSubjectSpecificGeneratedQuizWhenTopicHasNoQuiz() {
        Topic topic = Topic.builder()
                .id(100L)
                .code("JAVA_STRINGS_METHODS")
                .title("Strings and Methods")
                .estimatedHours(BigDecimal.valueOf(7))
                .build();
        List<QuizQuestion> savedQuestions = new ArrayList<>();
        List<QuizOption> savedOptions = new ArrayList<>();
        AtomicLong questionIds = new AtomicLong(200L);
        AtomicLong optionIds = new AtomicLong(300L);

        when(quizRepository.findByTopic_Id(100L)).thenReturn(Optional.empty());
        when(topicRepository.findById(100L)).thenReturn(Optional.of(topic));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> {
            Quiz quiz = invocation.getArgument(0);
            quiz.setId(10L);
            return quiz;
        });
        when(questionRepository.save(any(QuizQuestion.class))).thenAnswer(invocation -> {
            QuizQuestion question = invocation.getArgument(0);
            question.setId(questionIds.getAndIncrement());
            savedQuestions.add(question);
            return question;
        });
        when(optionRepository.save(any(QuizOption.class))).thenAnswer(invocation -> {
            QuizOption option = invocation.getArgument(0);
            option.setId(optionIds.getAndIncrement());
            savedOptions.add(option);
            return option;
        });
        when(questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(10L)).thenReturn(savedQuestions);
        when(optionRepository.findAllByQuestion_Id(any())).thenAnswer(invocation -> {
            Long questionId = invocation.getArgument(0);
            return savedOptions.stream()
                    .filter(option -> option.getQuestion().getId().equals(questionId))
                    .toList();
        });

        var response = quizService.getTopicQuiz(100L);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.status()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(response.title()).contains("Проверка по теме");
        assertThat(response.questions()).hasSize(3);
        assertThat(response.questions().get(0).text()).contains("строки");
        assertThat(response.questions().get(0).options())
                .anySatisfy(option -> assertThat(option.text()).contains("String неизменяемый"));
        assertThat(response.questions())
                .allSatisfy(question -> assertThat(question.options()).hasSize(3));
        verify(quizRepository).save(any(Quiz.class));
    }
}
