package com.example.adaprivelearningnavigator.service.dto.quiz;

import lombok.Builder;

@Builder
public record QuizOptionResponse(
        Long id,
        String text
        // isCorrect НЕ отдаём клиенту, иначе это “подсказка”
) {}
