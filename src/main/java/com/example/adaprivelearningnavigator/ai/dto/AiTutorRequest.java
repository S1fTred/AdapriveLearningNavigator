package com.example.adaprivelearningnavigator.ai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AiTutorRequest(
        @NotBlank(message = "Вопрос обязателен")
        @Size(max = 1200, message = "Вопрос слишком длинный")
        String question,

        @Size(max = 12, message = "История диалога слишком длинная")
        List<@Valid AiTutorMessage> history
) {
    public record AiTutorMessage(
            @NotBlank(message = "Роль сообщения обязательна")
            @Pattern(regexp = "user|assistant", message = "Роль сообщения должна быть user или assistant")
            String role,

            @NotBlank(message = "Текст сообщения обязателен")
            @Size(max = 2400, message = "Сообщение истории слишком длинное")
            String content
    ) {
    }
}
