package com.example.adaprivelearningnavigator.service.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token РѕР±СЏР·Р°С‚РµР»РµРЅ")
        String refreshToken
) {
}
