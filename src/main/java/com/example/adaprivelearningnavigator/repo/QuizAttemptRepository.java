package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findAllByUser_IdOrderBySubmittedAtDesc(Long userId);

    List<QuizAttempt> findAllByUser_IdAndQuiz_IdOrderBySubmittedAtDesc(Long userId, Long quizId);
}
