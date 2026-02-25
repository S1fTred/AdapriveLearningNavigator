package com.example.adaprivelearningnavigator.service.dto.role_goal;


import jakarta.validation.constraints.NotNull;

public record RoleTopicRequest(
        @NotNull(message = "id темы обязателен")
        Long topicId,

        Integer priority,

        @NotNull(message = "Признак 'обязательная тема' обязателен")
        Boolean isRequired
) {}
