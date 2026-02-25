package com.example.adaprivelearningnavigator.service.dto.resource;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import lombok.Builder;

@Builder
public record ResourceResponse(
        Long id,
        String title,
        String url,
        ResourceType type,
        String language,
        Integer durationMinutes,
        String provider,
        String difficulty,
        EntityStatus status
) {}
