package com.example.adaprivelearningnavigator.service.dto.user;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserProfileResponse(
        Long id,
        String email,
        String displayName,
        Instant createdAt
) {}
