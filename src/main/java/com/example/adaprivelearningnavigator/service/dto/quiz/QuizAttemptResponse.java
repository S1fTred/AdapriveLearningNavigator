package com.example.adaprivelearningnavigator.service.dto.quiz;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record QuizAttemptResponse(
        Long attemptId,
        Long quizId,
        BigDecimal score,
        Integer correctCount,
        Integer totalCount,
        Instant submittedAt
) {}
