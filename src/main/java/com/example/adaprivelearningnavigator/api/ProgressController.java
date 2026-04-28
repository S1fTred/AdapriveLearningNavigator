package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.ProgressService;
import com.example.adaprivelearningnavigator.service.dto.progress.TopicProgressResponse;
import com.example.adaprivelearningnavigator.service.dto.progress.TopicProgressUpsertRequest;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plans/{planId}/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public List<TopicProgressResponse> getPlanProgress(@PathVariable Long planId,
                                                       Authentication authentication) {
        return progressService.getPlanProgress(requireUserId(authentication), planId);
    }

    @PutMapping
    public TopicProgressResponse upsertTopicProgress(@PathVariable Long planId,
                                                     @Valid @RequestBody TopicProgressUpsertRequest request,
                                                     Authentication authentication) {
        return progressService.upsertTopicProgress(requireUserId(authentication), planId, request);
    }

    private Long requireUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthException("Требуется авторизация");
        }
        return principal.getUserId();
    }
}
