package com.example.adaprivelearningnavigator.service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(new TestExceptionController())
                .setControllerAdvice(new GlobalExceptionHandler(Clock.fixed(Instant.parse("2026-04-09T08:00:00Z"), ZoneOffset.UTC)))
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void shouldHandleNotFound() throws Exception {
        mockMvc.perform(get("/test-ex/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("not found"));
    }

    @Test
    void shouldHandleConflict() throws Exception {
        mockMvc.perform(get("/test-ex/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("conflict"));
    }

    @Test
    void shouldHandleForbidden() throws Exception {
        mockMvc.perform(get("/test-ex/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("forbidden"));
    }

    @Test
    void shouldHandlePlanBuild() throws Exception {
        mockMvc.perform(get("/test-ex/plan-build"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("plan build failed"));
    }

    @Test
    void shouldHandleGraphCycle() throws Exception {
        mockMvc.perform(get("/test-ex/graph-cycle"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("graph cycle"));
    }

    @Test
    void shouldHandleDataIntegrityViolation() throws Exception {
        mockMvc.perform(get("/test-ex/data-integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Нарушение целостности данных"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/test-ex/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Внутренняя ошибка сервера"));
    }

    @Test
    void shouldHandleMalformedJson() throws Exception {
        mockMvc.perform(post("/test-ex/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Некорректный JSON в запросе"));
    }

    @Test
    void shouldHandleMissingRequestParameter() throws Exception {
        mockMvc.perform(get("/test-ex/param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Пропущен обязательный параметр: value"));
    }

    @Test
    void shouldHandleMethodNotSupported() throws Exception {
        mockMvc.perform(post("/test-ex/param"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value("Метод не поддерживается: POST"));
    }

    @Test
    void shouldHandleUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/test-ex/body")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("plain"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").value("Неподдерживаемый тип данных запроса"));
    }

    @Test
    void shouldHandleValidationErrors() throws Exception {
        mockMvc.perform(post("/test-ex/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Payload(""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @RestController
    static class TestExceptionController {

        @GetMapping("/test-ex/not-found")
        public String notFound() {
            throw new NotFoundException("not found");
        }

        @GetMapping("/test-ex/conflict")
        public String conflict() {
            throw new ConflictException("conflict");
        }

        @GetMapping("/test-ex/forbidden")
        public String forbidden() {
            throw new ForbiddenException("forbidden");
        }

        @GetMapping("/test-ex/plan-build")
        public String planBuild() {
            throw new PlanBuildException("plan build failed");
        }

        @GetMapping("/test-ex/graph-cycle")
        public String graphCycle() {
            throw new GraphCycleException("graph cycle");
        }

        @GetMapping("/test-ex/data-integrity")
        public String dataIntegrity() {
            throw new DataIntegrityViolationException("db");
        }

        @GetMapping("/test-ex/unexpected")
        public String unexpected() {
            throw new RuntimeException("boom");
        }

        @GetMapping("/test-ex/param")
        public String param(@RequestParam String value) {
            return value;
        }

        @PostMapping(value = "/test-ex/body", consumes = MediaType.APPLICATION_JSON_VALUE)
        public String body(@Valid @RequestBody Payload payload) {
            return payload.value();
        }
    }

    record Payload(@NotBlank String value) {
    }
}
