package com.example.adaprivelearningnavigator.service.dto.plan;

import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record PlanShortResponse(
        Long id,
        Long roleId,
        String status,
        ScenarioType scenarioType,
        String scenarioLabel,
        Long basePlanId,
        Instant createdAt
) {}