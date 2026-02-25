package com.example.adaprivelearningnavigator.service.dto.plan;

import com.example.adaprivelearningnavigator.domain.enums.ScenarioType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record PlanFullResponse(
        Long id,
        Long roleId,
        String status,
        ScenarioType scenarioType,
        String scenarioLabel,
        Long basePlanId,
        Instant createdAt,
        PlanParamsSnapshotResponse params,
        List<PlanWeekResponse> weeks
) {}