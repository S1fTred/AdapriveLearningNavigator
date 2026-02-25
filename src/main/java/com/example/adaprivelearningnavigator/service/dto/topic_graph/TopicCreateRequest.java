package com.example.adaprivelearningnavigator.service.dto.topic_graph;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;

import java.math.BigDecimal;

public record TopicCreateRequest(
        @NotBlank(message = "Код темы обязателен")
        @Size(max = 50, message = "Код темы слишком длинный")
        String code,

        @NotBlank(message = "Название темы обязательно")
        @Size(max = 200, message = "Название темы слишком длинное")
        String title,

        String description,

        TopicLevel level,

        @NotNull(message = "Признак 'основная тема' обязателен")
        Boolean isCore,

        BigDecimal estimatedHours,

        EntityStatus status
) {}