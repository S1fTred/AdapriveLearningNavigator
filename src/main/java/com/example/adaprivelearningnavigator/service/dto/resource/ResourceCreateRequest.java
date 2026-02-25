package com.example.adaprivelearningnavigator.service.dto.resource;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record ResourceCreateRequest(
        @NotBlank(message = "Название ресурса обязательно")
        @Size(max = 255, message = "Название ресурса слишком длинное")
        String title,

        @NotBlank(message = "URL ресурса обязателен")
        @Size(max = 1000, message = "URL слишком длинный")
        String url,

        ResourceType type,
        String language,
        Integer durationMinutes,

        @Size(max = 120, message = "Название провайдера слишком длинное")
        String provider,

        @Size(max = 20, message = "Сложность слишком длинная")
        String difficulty,

        EntityStatus status
) {}
