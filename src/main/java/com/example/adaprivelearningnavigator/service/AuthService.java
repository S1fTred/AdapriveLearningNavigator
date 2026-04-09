package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.auth.AuthRequest;
import com.example.adaprivelearningnavigator.service.dto.auth.AuthResponse;
import com.example.adaprivelearningnavigator.service.dto.auth.RefreshTokenRequest;
import com.example.adaprivelearningnavigator.service.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
}
