package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.quizAndProgress.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByTopic_Id(Long topicId);
}
