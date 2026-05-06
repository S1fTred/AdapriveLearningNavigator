package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.PlanService;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanBuildRequest;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanShortResponse;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public PageResponse<PlanShortResponse> getPlans(Authentication authentication,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return planService.getPlans(requireUserId(authentication), page, size);
    }

    @GetMapping("/{planId}")
    public PlanFullResponse getPlan(@PathVariable Long planId,
                                    Authentication authentication) {
        return planService.getPlan(requireUserId(authentication), planId);
    }

    @DeleteMapping("/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable Long planId,
                           Authentication authentication) {
        planService.deletePlan(requireUserId(authentication), planId);
    }

    @PostMapping("/generate-with-ai")
    public PlanFullResponse generateWithAi(@Valid @RequestBody AiPlanGenerateRequest request,
                                           Authentication authentication) {
        return planService.generatePlanWithAi(requireUserId(authentication), request);
    }

    @PostMapping("/build-from-roadmap")
    public PlanFullResponse buildFromRoadmap(@Valid @RequestBody PlanBuildRequest request,
                                             Authentication authentication) {
        return planService.buildPlanFromRoadmap(requireUserId(authentication), request);
    }

    private Long requireUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthException("Требуется авторизация");
        }
        return principal.getUserId();
    }
}
