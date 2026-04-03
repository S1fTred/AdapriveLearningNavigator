package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.quiz.QuizAttemptResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizQuestionResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizResponse;
import com.example.adaprivelearningnavigator.service.dto.quiz.QuizSubmitRequest;

import java.util.List;

public interface QuizService {
    QuizResponse getTopicQuiz(Long topicId);

    List<QuizQuestionResponse> getQuizQuestions(Long quizId);

    QuizAttemptResponse submitQuiz(Long userId, QuizSubmitRequest request);

    List<QuizAttemptResponse> getAttempts(Long userId, Long quizId);
}
