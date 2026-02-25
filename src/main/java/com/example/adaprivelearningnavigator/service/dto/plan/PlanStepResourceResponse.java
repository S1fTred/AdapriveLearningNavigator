package com.example.adaprivelearningnavigator.service.dto.plan;

import lombok.Builder;

@Builder
public record PlanStepResourceResponse(
        Long resourceId,
        boolean isPrimary
) {}