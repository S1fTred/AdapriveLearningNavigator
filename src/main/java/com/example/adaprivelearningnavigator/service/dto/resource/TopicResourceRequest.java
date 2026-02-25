package com.example.adaprivelearningnavigator.service.dto.resource;

import jakarta.validation.constraints.NotNull;

public record TopicResourceRequest(
        @NotNull(message = "id темы обязателен")
        Long topicId,

        @NotNull(message = "id ресурса обязателен")
        Long resourceId,

        Integer rank
) {}