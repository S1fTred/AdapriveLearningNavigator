package com.example.adaprivelearningnavigator.service.dto.quiz;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record QuizResponse(
        Long id,
        Long topicId,
        String title,
        EntityStatus status,
        List<QuizQuestionResponse> questions
) {}
