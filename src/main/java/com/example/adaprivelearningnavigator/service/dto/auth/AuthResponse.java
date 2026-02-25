package com.example.adaprivelearningnavigator.service.dto.auth;


public record AuthResponse(
        String token,
        String tokenType // "Bearer"
) {}
