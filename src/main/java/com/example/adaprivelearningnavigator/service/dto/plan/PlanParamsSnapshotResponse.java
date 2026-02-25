package com.example.adaprivelearningnavigator.service.dto.plan;

import lombok.Builder;

@Builder
public record PlanParamsSnapshotResponse(
        Integer hoursPerWeek,
        String prefsLanguage,
        String prefsResourceTypes,
        String algoVersion
) {}
