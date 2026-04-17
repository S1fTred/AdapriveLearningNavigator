package com.example.adaprivelearningnavigator.service.dto.roadmap;

import lombok.Builder;

@Builder
public record RoadmapQuizSummaryResponse(
        Long id,
        String title,
        boolean available
) {
}
