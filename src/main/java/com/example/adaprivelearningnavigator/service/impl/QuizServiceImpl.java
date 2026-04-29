package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.QuizQuestionType;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.Quiz;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizAttempt;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizOption;
import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizQuestion;
import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.QuizAttemptRepository;
import com.example.adaprivelearningnavigator.repo.QuizOptionRepository;
import com.example.adaprivelearningnavigator.repo.QuizQuestionRepository;
import com.example.adaprivelearningnavigator.repo.QuizRepository;
import com.example.adaprivelearningnavigator.repo.TopicRepository;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.service.QuizService;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizAttemptResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizOptionResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizQuestionResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizSubmitRequest;
import com.example.adaprivelearningnavigator.service.exception.BadRequestException;
import com.example.adaprivelearningnavigator.service.exception.NotFoundException;
import com.example.adaprivelearningnavigator.service.support.KnowledgeBaseLocalizationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository questionRepository;
    private final QuizOptionRepository optionRepository;
    private final QuizAttemptRepository attemptRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public QuizServiceImpl(QuizRepository quizRepository,
                           QuizQuestionRepository questionRepository,
                           QuizOptionRepository optionRepository,
                           QuizAttemptRepository attemptRepository,
                           TopicRepository topicRepository,
                           UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.attemptRepository = attemptRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public QuizResponse getTopicQuiz(Long topicId) {
        Quiz quiz = quizRepository.findByTopic_Id(topicId)
                .orElseGet(() -> createGeneratedQuiz(topicId));
        return toQuizResponse(quiz, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizQuestionResponse> getQuizQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Квиз не найден"));
        return questionsFor(quiz);
    }

    @Override
    @Transactional
    public QuizAttemptResponse submitQuiz(Long userId, QuizSubmitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Quiz quiz = quizRepository.findById(request.quizId())
                .orElseThrow(() -> new NotFoundException("Квиз не найден"));

        List<QuizQuestion> questions = questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(quiz.getId());
        if (questions.isEmpty()) {
            throw new BadRequestException("В квизе пока нет вопросов");
        }

        Set<Long> selectedOptionIds = request.selectedOptionIds();
        int correctCount = 0;
        for (QuizQuestion question : questions) {
            Set<Long> correctOptionIds = optionRepository.findAllByQuestion_Id(question.getId()).stream()
                    .filter(QuizOption::isCorrect)
                    .map(QuizOption::getId)
                    .collect(java.util.stream.Collectors.toSet());
            Set<Long> selectedForQuestion = optionRepository.findAllByQuestion_Id(question.getId()).stream()
                    .map(QuizOption::getId)
                    .filter(selectedOptionIds::contains)
                    .collect(java.util.stream.Collectors.toSet());
            if (selectedForQuestion.equals(correctOptionIds)) {
                correctCount++;
            }
        }

        BigDecimal score = BigDecimal.valueOf(correctCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(questions.size()), 2, RoundingMode.HALF_UP);

        QuizAttempt attempt = attemptRepository.save(QuizAttempt.builder()
                .user(user)
                .quiz(quiz)
                .score(score)
                .correctCount(correctCount)
                .totalCount(questions.size())
                .submittedAt(Instant.now())
                .build());

        return toAttemptResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizAttemptResponse> getAttempts(Long userId, Long quizId) {
        if (quizId == null) {
            return attemptRepository.findAllByUser_IdOrderBySubmittedAtDesc(userId).stream()
                    .map(this::toAttemptResponse)
                    .toList();
        }
        return attemptRepository.findAllByUser_IdAndQuiz_IdOrderBySubmittedAtDesc(userId, quizId).stream()
                .map(this::toAttemptResponse)
                .toList();
    }

    private Quiz createGeneratedQuiz(Long topicId) {
        var topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Тема не найдена"));
        String title = KnowledgeBaseLocalizationUtil.localizeTopicTitle(topic.getCode(), topic.getTitle());
        Quiz quiz = quizRepository.save(Quiz.builder()
                .topic(topic)
                .title("Проверка по теме: " + title)
                .status(EntityStatus.ACTIVE)
                .build());

        createSingleQuestion(quiz, 1,
                "Что лучше всего сделать при изучении темы «" + title + "»?",
                "Разобрать ключевые понятия и закрепить их на практике",
                "Пропустить теорию и сразу перейти к следующей теме",
                "Выучить только определения без примеров");
        createSingleQuestion(quiz, 2,
                "Как понять, что тема «" + title + "» освоена?",
                "Вы можете объяснить её простыми словами и применить в небольшой задаче",
                "Вы один раз прочитали название темы",
                "Вы открыли все ссылки, но ничего не пробовали");
        createSingleQuestion(quiz, 3,
                "Какой формат работы обычно полезнее для закрепления темы?",
                "Короткий конспект, пример и самостоятельная практика",
                "Только пассивный просмотр материалов",
                "Переход к сложным темам без проверки понимания");

        return quiz;
    }

    private void createSingleQuestion(Quiz quiz,
                                      int orderIndex,
                                      String text,
                                      String correct,
                                      String firstWrong,
                                      String secondWrong) {
        QuizQuestion question = questionRepository.save(QuizQuestion.builder()
                .quiz(quiz)
                .text(text)
                .type(QuizQuestionType.SINGLE)
                .orderIndex(orderIndex)
                .build());
        optionRepository.save(QuizOption.builder().question(question).text(correct).correct(true).build());
        optionRepository.save(QuizOption.builder().question(question).text(firstWrong).correct(false).build());
        optionRepository.save(QuizOption.builder().question(question).text(secondWrong).correct(false).build());
    }

    private QuizResponse toQuizResponse(Quiz quiz, boolean includeQuestions) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .topicId(quiz.getTopic().getId())
                .title(quiz.getTitle())
                .status(quiz.getStatus())
                .questions(includeQuestions ? questionsFor(quiz) : List.of())
                .build();
    }

    private List<QuizQuestionResponse> questionsFor(Quiz quiz) {
        return questionRepository.findAllByQuiz_IdOrderByOrderIndexAsc(quiz.getId()).stream()
                .map(question -> QuizQuestionResponse.builder()
                        .id(question.getId())
                        .text(question.getText())
                        .type(question.getType())
                        .orderIndex(question.getOrderIndex())
                        .options(optionRepository.findAllByQuestion_Id(question.getId()).stream()
                                .map(option -> QuizOptionResponse.builder()
                                        .id(option.getId())
                                        .text(option.getText())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }

    private QuizAttemptResponse toAttemptResponse(QuizAttempt attempt) {
        return QuizAttemptResponse.builder()
                .attemptId(attempt.getId())
                .quizId(attempt.getQuiz().getId())
                .score(attempt.getScore())
                .correctCount(attempt.getCorrectCount())
                .totalCount(attempt.getTotalCount())
                .submittedAt(attempt.getSubmittedAt())
                .build();
    }
}
