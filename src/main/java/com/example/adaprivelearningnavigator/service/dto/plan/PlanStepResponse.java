package com.example.adaprivelearningnavigator.service.dto.plan;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PlanStepResponse(
        Long id,
        Long topicId,
        String topicCode,
        String topicTitle,
        int orderInWeek,
        BigDecimal plannedHours,
        boolean isOptional,
        List<PlanStepResourceResponse> resources,
        PlanStepExplanationResponse explanation
) {}
