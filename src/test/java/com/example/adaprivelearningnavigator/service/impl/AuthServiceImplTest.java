package com.example.adaprivelearningnavigator.service.impl;

import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.security.JwtService;
import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.dto.auth.AuthRequest;
import com.example.adaprivelearningnavigator.service.dto.auth.AuthResponse;
import com.example.adaprivelearningnavigator.service.dto.auth.RefreshTokenRequest;
import com.example.adaprivelearningnavigator.service.dto.auth.RegisterRequest;
import com.example.adaprivelearningnavigator.service.exception.AuthException;
import com.example.adaprivelearningnavigator.service.exception.ConflictException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @Test
    void shouldRegisterUserAndReturnTokens() {
        RegisterRequest request = new RegisterRequest("USER@Example.com", "StrongPass123", "Tester");
        when(userRepository.existsByEmailIgnoreCase("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return user;
        });
        when(jwtService.generateAccessToken(any(UserPrincipal.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(UserPrincipal.class))).thenReturn("refresh-token");

        AuthServiceImpl service = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);

        AuthResponse response = service.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("user@example.com", savedUser.getEmail());
        assertEquals("encoded-password", savedUser.getPasswordHash());
        assertEquals("Tester", savedUser.getDisplayName());
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals("Bearer", response.tokenType());
    }

    @Test
    void shouldRejectDuplicateEmailOnRegister() {
        when(userRepository.existsByEmailIgnoreCase("user@example.com")).thenReturn(true);
        AuthServiceImpl service = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);

        assertThrows(ConflictException.class,
                () -> service.register(new RegisterRequest("user@example.com", "StrongPass123", "Tester")));
    }

    @Test
    void shouldLoginWithValidCredentials() {
        User user = User.builder()
                .id(20L)
                .email("user@example.com")
                .passwordHash("encoded-password")
                .displayName("Tester")
                .createdAt(Instant.now())
                .build();

        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("StrongPass123", "encoded-password")).thenReturn(true);
        when(jwtService.generateAccessToken(any(UserPrincipal.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(UserPrincipal.class))).thenReturn("refresh-token");

        AuthServiceImpl service = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);

        AuthResponse response = service.login(new AuthRequest("USER@example.com", "StrongPass123"));

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }

    @Test
    void shouldRejectInvalidPasswordOnLogin() {
        User user = User.builder()
                .id(20L)
                .email("user@example.com")
                .passwordHash("encoded-password")
                .displayName("Tester")
                .createdAt(Instant.now())
                .build();

        when(userRepository.findByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        AuthServiceImpl service = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);

        assertThrows(AuthException.class, () -> service.login(new AuthRequest("user@example.com", "wrong-password")));
    }

    @Test
    void shouldRefreshTokensForValidRefreshToken() {
        Claims claims = Jwts.claims();
        claims.put("userId", 20L);
        User user = User.builder()
                .id(20L)
                .email("user@example.com")
                .passwordHash("encoded-password")
                .displayName("Tester")
                .createdAt(Instant.now())
                .build();

        when(jwtService.parseAndValidateRefreshToken("refresh-token")).thenReturn(claims);
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(any(UserPrincipal.class))).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(any(UserPrincipal.class))).thenReturn("new-refresh-token");

        AuthServiceImpl service = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);

        AuthResponse response = service.refresh(new RefreshTokenRequest("refresh-token"));

        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void shouldRejectInvalidRefreshToken() {
        when(jwtService.parseAndValidateRefreshToken(anyString())).thenThrow(new JwtException("bad token"));
        AuthServiceImpl service = new AuthServiceImpl(userRepository, passwordEncoder, jwtService);

        assertThrows(AuthException.class, () -> service.refresh(new RefreshTokenRequest("bad-token")));
    }
}
