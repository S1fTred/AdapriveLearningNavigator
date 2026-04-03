package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.user.UserProfileResponse;

public interface UserService {
    UserProfileResponse getCurrentProfile(Long userId);
}
