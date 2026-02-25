package com.example.adaprivelearningnavigator.service.dto.user;

import lombok.Builder;

@Builder
public record UserProfileResponse(
        Long id,
        String displayName
) {}
