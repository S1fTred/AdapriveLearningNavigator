package com.example.adaprivelearningnavigator.security;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(UserPrincipal principal);
    String generateAccessToken(UserPrincipal principal);
    String generateRefreshToken(UserPrincipal principal);
    Claims parseAndValidate(String token);
    Claims parseAndValidateAccessToken(String token);
    Claims parseAndValidateRefreshToken(String token);
}
