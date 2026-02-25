package com.example.adaprivelearningnavigator.security;

import com.example.adaprivelearningnavigator.service.exception.ApiErrorResponse;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        HttpStatus status = HttpStatus.FORBIDDEN;

        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(clock),
                status.value(),
                status.getReasonPhrase(),
                "Недостаточно прав для выполнения операции",
                request.getRequestURI(),
                null
        );

        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}