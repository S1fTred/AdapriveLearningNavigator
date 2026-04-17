package com.example.adaprivelearningnavigator.service.dto.roadmap;

import com.example.adaprivelearningnavigator.domain.enums.TopicLevel;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record RoadmapTopicDetailResponse(
        Long roleId,
        String roleCode,
        String roleName,
        Long topicId,
        String topicCode,
        String topicTitle,
        String description,
        TopicLevel level,
        boolean isCore,
        BigDecimal estimatedHours,
        Integer priority,
        boolean isRequired,
        List<RoadmapTopicResponse> prereqs,
        List<RoadmapTopicResponse> unlocks,
        List<RoadmapResourceResponse> resources,
        RoadmapQuizSummaryResponse quiz
) {
}
