package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.ai.dto.AiTutorRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiTutorResponse;
import com.example.adaprivelearningnavigator.ai.service.AiTutorService;
import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roadmaps/{roleId}/topics/{topicId}/tutor")
public class AiTutorController {

    private final AiTutorService aiTutorService;

    public AiTutorController(AiTutorService aiTutorService) {
        this.aiTutorService = aiTutorService;
    }

    @PostMapping
    public AiTutorResponse ask(@PathVariable Long roleId,
                               @PathVariable Long topicId,
                               @Valid @RequestBody AiTutorRequest request,
                               Authentication authentication) {
        requireAuthenticated(authentication);
        return aiTutorService.ask(roleId, topicId, request);
    }

    private void requireAuthenticated(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new AuthException("Требуется авторизация");
        }
    }
}
