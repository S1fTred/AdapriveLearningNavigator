package com.example.adaprivelearningnavigator.service.dto.user;

import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserPreferenceResponse(
        String preferredLanguage,
        Integer hoursPerWeekDefault,
        Set<ResourceType> preferredResourceTypes
) {}
