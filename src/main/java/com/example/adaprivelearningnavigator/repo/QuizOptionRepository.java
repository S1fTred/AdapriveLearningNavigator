package com.example.adaprivelearningnavigator.repo;

import com.example.adaprivelearningnavigator.domain.quizAndProgress.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
    List<QuizOption> findAllByQuestion_Id(Long questionId);

    List<QuizOption> findAllByQuestion_Quiz_Id(Long quizId);
}
