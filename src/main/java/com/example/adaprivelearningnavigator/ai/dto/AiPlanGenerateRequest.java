package com.example.adaprivelearningnavigator.ai.dto;

import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AiPlanGenerateRequest(
        @NotEmpty(message = "Цель обучения обязательна")
        String goal,

        @NotNull(message = "Текущий уровень обязателен")
        UserLevel currentLevel,

        @NotNull(message = "Часы в неделю обязательны")
        @Min(value = 1, message = "Часы в неделю должны быть не меньше 1")
        @Max(value = 80, message = "Часы в неделю должны быть не больше 80")
        Integer hoursPerWeek,

        @NotNull(message = "Список известных тем обязателен")
        Set<@NotBlank(message = "Название известной темы не может быть пустым") String> knownTopics
) {
}
