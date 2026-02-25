package com.example.adaprivelearningnavigator.service.dto.progress;

import com.example.adaprivelearningnavigator.domain.enums.ProgressStatus;
import jakarta.validation.constraints.NotNull;


public record TopicProgressUpsertRequest(
        @NotNull(message = "id темы обязателен")
        Long topicId,

        @NotNull(message = "Статус прогресса обязателен")
        ProgressStatus status
) {}