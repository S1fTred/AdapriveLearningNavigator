package com.example.adaprivelearningnavigator.service.dto.role_goal;


import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RoleGoalCreateRequest(
        @NotBlank(message = "Код роли обязателен")
        @Size(max = 50, message = "Код роли слишком длинный")
        String code,

        @NotBlank(message = "Название роли обязательно")
        @Size(max = 200, message = "Название роли слишком длинное")
        String name,

        String description,
        EntityStatus status
) {}
