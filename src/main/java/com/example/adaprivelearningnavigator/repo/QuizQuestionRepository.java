package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findAllByQuiz_IdOrderByOrderIndexAsc(Long quizId);
}
