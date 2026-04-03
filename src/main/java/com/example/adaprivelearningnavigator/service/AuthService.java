package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.auth.AuthRequest;
import com.example.adaprivelearningnavigator.service.dto.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
