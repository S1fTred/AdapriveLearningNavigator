package com.example.adaprivelearningnavigator.service.dto.roadmap;

import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record RoadmapTopicResponse(
        Long topicId,
        String topicCode,
        String topicTitle,
        String description,
        TopicLevel level,
        boolean isCore,
        BigDecimal estimatedHours,
        Integer priority,
        boolean isRequired,
        List<Long> prereqTopicIds,
        List<String> prereqTopicCodes
) {
}
