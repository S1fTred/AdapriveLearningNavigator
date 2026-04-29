package com.example.adaprivelearningnavigator.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AiTutorRequest(
        @NotBlank(message = "Вопрос обязателен")
        @Size(max = 1200, message = "Вопрос слишком длинный")
        String question
) {
}
