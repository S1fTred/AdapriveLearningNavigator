package com.example.adaprivelearningnavigator.service.dto.quiz;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record QuizSubmitRequest(
        @NotNull(message = "id квиза обязателен")
        Long quizId,

        // выбранные варианты (для single тоже Set, но валидируем на сервисе)
        @NotNull(message = "Выбранные ответы обязательны")
        Set<Long> selectedOptionIds
) {}
