package com.example.adaprivelearningnavigator.security;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(UserPrincipal principal);
    Claims parseAndValidate(String token);
}
