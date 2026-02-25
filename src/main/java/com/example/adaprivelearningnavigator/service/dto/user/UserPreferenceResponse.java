package com.example.adaprivelearningnavigator.service.dto.user;

import lombok.Builder;

@Builder
public record UserPreferenceResponse(
        String preferredLanguage,
        Integer hoursPerWeekDefault,
        String preferredResourceTypes
) {}
