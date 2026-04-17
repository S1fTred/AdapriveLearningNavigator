package com.example.adaprivelearningnavigator.api;

import com.example.adaprivelearningnavigator.ai.dto.AiPlanGenerateRequest;
import com.example.adaprivelearningnavigator.domain.enums.UserLevel;
import com.example.adaprivelearningnavigator.security.UserPrincipal;
import com.example.adaprivelearningnavigator.service.PlanService;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanBuildRequest;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanFullResponse;
import com.example.adaprivelearningnavigator.service.dto.plan.PlanShortResponse;
import com.example.adaprivelearningnavigator.service.exception.AiRouteGenerationException;
import com.example.adaprivelearningnavigator.service.exception.AiRouteValidationException;
import com.example.adaprivelearningnavigator.service.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Clock;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlanControllerTest {

    @Mock
    private PlanService planService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(new PlanController(planService))
                .setControllerAdvice(new GlobalExceptionHandler(Clock.systemUTC()))
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void shouldReturnUnauthorizedWhenAuthenticationMissing() throws Exception {
        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldGeneratePlanWhenAuthenticationPresent() throws Exception {
        when(planService.generatePlanWithAi(eq(42L), any(AiPlanGenerateRequest.class)))
                .thenReturn(PlanFullResponse.builder()
                        .id(100L)
                        .roleId(200L)
                        .roleCode("java-backend")
                        .roleName("Java Backend Developer")
                        .weeks(List.of())
                        .build());

        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .principal(authentication())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.roleId").value(200))
                .andExpect(jsonPath("$.roleCode").value("java-backend"));
    }

    @Test
    void shouldReturnPlansPageWhenAuthenticationPresent() throws Exception {
        when(planService.getPlans(42L, 0, 10))
                .thenReturn(PageResponse.<PlanShortResponse>builder()
                        .items(List.of(PlanShortResponse.builder()
                                .id(100L)
                                .roleId(200L)
                                .roleCode("java-backend")
                                .roleName("Java Backend Developer")
                                .status("DRAFT")
                                .build()))
                        .total(1)
                        .page(0)
                        .size(10)
                        .build());

        mockMvc.perform(get("/api/plans")
                        .principal(authentication()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].roleCode").value("java-backend"))
                .andExpect(jsonPath("$.items[0].roleName").value("Java Backend Developer"));
    }

    @Test
    void shouldReturnSinglePlanWhenAuthenticationPresent() throws Exception {
        when(planService.getPlan(42L, 100L))
                .thenReturn(PlanFullResponse.builder()
                        .id(100L)
                        .roleId(200L)
                        .roleCode("java-backend")
                        .roleName("Java Backend Developer")
                        .weeks(List.of())
                        .build());

        mockMvc.perform(get("/api/plans/100")
                        .principal(authentication()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleCode").value("java-backend"))
                .andExpect(jsonPath("$.roleName").value("Java Backend Developer"));
    }

    @Test
    void shouldReturnBadRequestForInvalidBody() throws Exception {
        AiPlanGenerateRequest invalidRequest = new AiPlanGenerateRequest(
                "",
                UserLevel.BEGINNER,
                0,
                Set.of(" ")
        );

        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .principal(authentication())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnServiceUnavailableWhenAiGenerationFails() throws Exception {
        when(planService.generatePlanWithAi(eq(42L), any(AiPlanGenerateRequest.class)))
                .thenThrow(new AiRouteGenerationException("AI недоступен"));

        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .principal(authentication())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("AI недоступен"));
    }

    @Test
    void shouldReturnBadRequestWhenAiValidationFails() throws Exception {
        when(planService.generatePlanWithAi(eq(42L), any(AiPlanGenerateRequest.class)))
                .thenThrow(new AiRouteValidationException("Неизвестная тема"));

        mockMvc.perform(post("/api/plans/generate-with-ai")
                        .principal(authentication())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Неизвестная тема"));
    }

    @Test
    void shouldBuildPlanFromRoadmapWhenAuthenticationPresent() throws Exception {
        when(planService.buildPlanFromRoadmap(eq(42L), any(PlanBuildRequest.class)))
                .thenReturn(PlanFullResponse.builder()
                        .id(101L)
                        .roleId(200L)
                        .roleCode("java-backend")
                        .roleName("Java Backend Developer")
                        .weeks(List.of())
                        .build());

        mockMvc.perform(post("/api/plans/build-from-roadmap")
                        .principal(authentication())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PlanBuildRequest(
                                200L,
                                8,
                                Set.of(10L, 11L),
                                null
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.roleCode").value("java-backend"));
    }

    private UsernamePasswordAuthenticationToken authentication() {
        UserPrincipal principal = new UserPrincipal(42L, "test@example.com");
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    private AiPlanGenerateRequest validRequest() {
        return new AiPlanGenerateRequest(
                "Java backend developer",
                UserLevel.BEGINNER,
                8,
                Set.of("Git")
        );
    }
}
