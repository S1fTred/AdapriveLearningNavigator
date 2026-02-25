package com.example.adaprivelearningnavigator.service.dto.topic_graph;

import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TopicUpdateRequest(
        @Size(max = 200, message = "Название темы слишком длинное")
        String title,

        String description,

        TopicLevel level,
        Boolean isCore,
        BigDecimal estimatedHours,
        EntityStatus status
) {}
