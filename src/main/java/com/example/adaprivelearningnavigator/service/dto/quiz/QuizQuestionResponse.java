package com.example.adaprivelearningnavigator.service.dto.quiz;


import com.example.adaprivelearningnavigator.domain.enums.QuizQuestionType;
import lombok.Builder;

import java.util.List;

@Builder
public record QuizQuestionResponse(
        Long id,
        String text,
        QuizQuestionType type,
        int orderIndex,
        List<QuizOptionResponse> options
) {}
