package com.example.adaprivelearningnavigator.service.dto.role_goal;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import lombok.Builder;

@Builder
public record RoleGoalResponse(
        Long id,
        String code,
        String name,
        String description,
        EntityStatus status
) {}
