package com.example.adaprivelearningnavigator.ai.dto;

import lombok.Builder;

@Builder
public record AiTutorResponse(
        Long roleId,
        Long topicId,
        String topicTitle,
        String answer
) {
}
