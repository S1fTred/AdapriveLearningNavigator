package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.user.UserPreferenceRequest;
import com.example.adaprivelearningnavigator.service.dto.user.UserPreferenceResponse;

public interface UserPreferenceService {
    UserPreferenceResponse getPreferences(Long userId);

    UserPreferenceResponse upsertPreferences(Long userId, UserPreferenceRequest request);
}
