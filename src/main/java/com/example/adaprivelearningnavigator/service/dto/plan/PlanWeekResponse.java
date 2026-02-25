package com.example.adaprivelearningnavigator.service.dto.plan;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PlanWeekResponse(
        Long id,
        int weekIndex,
        BigDecimal hoursBudget,
        BigDecimal hoursPlanned,
        List<PlanStepResponse> steps
) {}