package com.example.adaprivelearningnavigator.service.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token обязателен")
        String refreshToken
) {
}
