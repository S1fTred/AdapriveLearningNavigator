package com.example.adaprivelearningnavigator.service.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType // "Bearer"
) {}
