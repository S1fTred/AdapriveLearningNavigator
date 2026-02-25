package com.example.adaprivelearningnavigator.service.dto.topic_graph;


import com.example.adaprivelearningnavigator.domain.enums.EntityStatus;
import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TopicResponse(
        Long id,
        String code,
        String title,
        String description,
        TopicLevel level,
        boolean isCore,
        BigDecimal estimatedHours,
        EntityStatus status
) {}