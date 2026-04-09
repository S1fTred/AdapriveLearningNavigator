package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import com.example.adaprivelearningnavigator.security.JwtService;
import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.dto.auth.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserAndReturnAccessAndRefreshTokens() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "new-user@example.com",
                                  "password": "StrongPass123",
                                  "displayName": "New User"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void shouldRejectDuplicateEmailOnRegister() throws Exception {
        userRepository.save(User.builder()
                .email("new-user@example.com")
                .passwordHash(passwordEncoder.encode("StrongPass123"))
                .displayName("Existing User")
                .createdAt(Instant.now())
                .build());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "new-user@example.com",
                                  "password": "StrongPass123",
                                  "displayName": "New User"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        userRepository.save(User.builder()
                .email("login-user@example.com")
                .passwordHash(passwordEncoder.encode("StrongPass123"))
                .displayName("Login User")
                .createdAt(Instant.now())
                .build());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "login-user@example.com",
                                  "password": "StrongPass123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void shouldRejectLoginWithInvalidPassword() throws Exception {
        userRepository.save(User.builder()
                .email("login-user@example.com")
                .passwordHash(passwordEncoder.encode("StrongPass123"))
                .displayName("Login User")
                .createdAt(Instant.now())
                .build());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "login-user@example.com",
                                  "password": "WrongPass123"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldRefreshTokens() throws Exception {
        User user = userRepository.save(User.builder()
                .email("refresh-user@example.com")
                .passwordHash(passwordEncoder.encode("StrongPass123"))
                .displayName("Refresh User")
                .createdAt(Instant.now())
                .build());
        String refreshToken = jwtService.generateRefreshToken(UserPrincipal.from(user));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
                            put("refreshToken", refreshToken);
                        }})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.accessToken").value(not(refreshToken)))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void shouldRejectAccessTokenOnRefreshEndpoint() throws Exception {
        User user = userRepository.save(User.builder()
                .email("refresh-user@example.com")
                .passwordHash(passwordEncoder.encode("StrongPass123"))
                .displayName("Refresh User")
                .createdAt(Instant.now())
                .build());
        String accessToken = jwtService.generateAccessToken(UserPrincipal.from(user));

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new java.util.LinkedHashMap<>() {{
                            put("refreshToken", accessToken);
                        }})))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }
}
