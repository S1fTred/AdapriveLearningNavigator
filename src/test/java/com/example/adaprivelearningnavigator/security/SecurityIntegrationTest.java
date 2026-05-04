package com.example.adaprivelearningnavigator.security;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.service.PlanService;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.adaprivelearningnavigator.repo.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityIntegrationTest.PublicTopicTestController.class)
class SecurityIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @MockitoBean
    private PlanService planService;

    private User savedUser;
    private String validToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        savedUser = userRepository.save(User.builder()
                .email("secured@example.com")
                .passwordHash(passwordEncoder.encode("StrongPass123"))
                .displayName("Secured User")
                .createdAt(Instant.now())
                .build());
        validToken = jwtService.generateAccessToken(UserPrincipal.from(savedUser));
    }

    @Test
    void shouldRequireAuthenticationForTopicsEndpoint() throws Exception {
        mockMvc.perform(get("/api/topics/public-check"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldRejectProtectedEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldRejectTokenForMissingUser() throws Exception {
        User ghost = User.builder()
                .id(99999L)
                .email("ghost@example.com")
                .displayName("Ghost")
                .createdAt(Instant.now())
                .build();
        String ghostToken = jwtService.generateToken(UserPrincipal.from(ghost));

        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .header("Authorization", "Bearer " + ghostToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowProtectedEndpointWithValidToken() throws Exception {
        when(planService.generatePlanWithAi(eq(savedUser.getId()), any(AiPlanGenerateRequest.class)))
                .thenReturn(PlanFullResponse.builder()
                        .id(500L)
                        .roleId(200L)
                        .weeks(List.of())
                        .build());

        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .header("Authorization", "Bearer " + validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(500));
    }

    private AiPlanGenerateRequest validRequest() {
        return new AiPlanGenerateRequest(
                "Java backend developer",
                UserLevel.BEGINNER,
                8,
                Set.of("Git")
        );
    }

    @RestController
    static class PublicTopicTestController {

        @GetMapping("/api/topics/public-check")
        public java.util.Map<String, String> publicTopic() {
            return java.util.Map.of("message", "public");
        }
    }
}
