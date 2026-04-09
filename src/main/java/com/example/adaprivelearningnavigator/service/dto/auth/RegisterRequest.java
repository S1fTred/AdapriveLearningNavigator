package com.example.adaprivelearningnavigator.service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "Некорректный email")
        @NotBlank(message = "Email обязателен")
        String email,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 100, message = "Пароль должен содержать от 8 до 100 символов")
        String password,

        @NotBlank(message = "Имя отображения обязательно")
        @Size(max = 120, message = "Имя отображения не должно превышать 120 символов")
        String displayName
) {
}
