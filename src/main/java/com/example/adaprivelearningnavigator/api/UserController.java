package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.UserService;
import com.example.adaprivelearningnavigator.service.dto.user.UserProfileResponse;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileResponse getCurrentProfile(Authentication authentication) {
        return userService.getCurrentProfile(requireUserId(authentication));
    }

    private Long requireUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthException("Требуется авторизация");
        }
        return principal.getUserId();
    }
}
