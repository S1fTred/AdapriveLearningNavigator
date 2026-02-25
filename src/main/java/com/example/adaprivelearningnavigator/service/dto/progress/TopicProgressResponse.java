package com.example.adaprivelearningnavigator.service.dto.progress;


import com.example.adaprivelearningnavigator.domain.enums.ProgressStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TopicProgressResponse(
        Long topicId,
        ProgressStatus status,
        Instant updatedAt
) {}