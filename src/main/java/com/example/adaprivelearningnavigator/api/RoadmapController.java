package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.RoadmapService;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapDetailResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapSummaryResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapTopicDetailResponse;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @GetMapping
    public PageResponse<RoadmapSummaryResponse> getRoadmaps(Authentication authentication,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "12") int size) {
        requireAuthenticated(authentication);
        return roadmapService.getRoadmaps(page, size);
    }

    @GetMapping("/{roleId}")
    public RoadmapDetailResponse getRoadmap(@PathVariable Long roleId,
                                            Authentication authentication) {
        requireAuthenticated(authentication);
        return roadmapService.getRoadmap(roleId);
    }

    @GetMapping("/{roleId}/topics/{topicId}")
    public RoadmapTopicDetailResponse getRoadmapTopic(@PathVariable Long roleId,
                                                      @PathVariable Long topicId,
                                                      Authentication authentication) {
        requireAuthenticated(authentication);
        return roadmapService.getRoadmapTopic(roleId, topicId);
    }

    private void requireAuthenticated(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new AuthException("Требуется авторизация");
        }
    }
}
