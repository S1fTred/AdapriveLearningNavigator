package com.example.adaprivelearningnavigator.security;

import com.example.adaprivelearningnavigator.service.exception.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestSecurityHandlersTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-04-09T09:00:00Z"), ZoneOffset.UTC);

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    @Test
    void shouldRenderUnauthorizedJsonResponse() throws ServletException, IOException {
        RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint(objectMapper, clock);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/plans/generate-with-ai");
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response, new BadCredentialsException("bad credentials"));

        ApiErrorResponse body = objectMapper.readValue(response.getContentAsByteArray(), ApiErrorResponse.class);

        assertEquals(401, response.getStatus());
        assertEquals("application/json; charset=UTF-8", response.getContentType());
        assertEquals(401, body.status());
        assertEquals("Unauthorized", body.error());
        assertEquals("/api/plans/generate-with-ai", body.path());
        assertNotNull(body.message());
    }

    @Test
    void shouldRenderForbiddenJsonResponse() throws ServletException, IOException {
        RestAccessDeniedHandler accessDeniedHandler = new RestAccessDeniedHandler(objectMapper, clock);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/test/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        accessDeniedHandler.handle(request, response, new AccessDeniedException("denied"));

        ApiErrorResponse body = objectMapper.readValue(response.getContentAsByteArray(), ApiErrorResponse.class);

        assertEquals(403, response.getStatus());
        assertEquals("application/json; charset=UTF-8", response.getContentType());
        assertEquals(403, body.status());
        assertEquals("Forbidden", body.error());
        assertEquals("/api/test/admin", body.path());
        assertNotNull(body.message());
    }
}
