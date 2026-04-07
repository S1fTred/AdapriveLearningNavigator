package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.service.dto.common.IdResponse;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanBuildRequest;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanShortResponse;

public interface PlanService {
    PlanFullResponse generatePlanWithAi(Long userId, AiPlanGenerateRequest request);

    IdResponse buildPlan(Long userId, PlanBuildRequest request);

    IdResponse buildWhatIfPlan(Long userId, Long basePlanId, PlanBuildRequest request);

    PageResponse<PlanShortResponse> getPlans(Long userId, int page, int size);

    PlanFullResponse getPlan(Long userId, Long planId);
}
