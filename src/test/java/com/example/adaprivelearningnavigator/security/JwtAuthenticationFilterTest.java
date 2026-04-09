package com.example.adaprivelearningnavigator.security;

import com.example.adaprivelearningnavigator.domain.userPart.User;
import com.example.adaprivelearningnavigator.repo.UserRepository;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UserRepository userRepository;
    @Mock private AuthenticationEntryPoint authenticationEntryPoint;
    @Mock private FilterChain filterChain;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSkipWhitelistedRequests() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userRepository, authenticationEntryPoint);
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/topics/java");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).parseAndValidateAccessToken(any());
    }

    @Test
    void shouldAuthenticateValidBearerToken() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userRepository, authenticationEntryPoint);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/plans/generate-with-ai");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token-value");
        MockHttpServletResponse response = new MockHttpServletResponse();
        User user = User.builder()
                .id(42L)
                .email("test@example.com")
                .displayName("Test")
                .createdAt(Instant.now())
                .build();

        io.jsonwebtoken.Claims validClaims = io.jsonwebtoken.Jwts.claims();
        validClaims.put("userId", 42L);
        when(jwtService.parseAndValidateAccessToken("token-value")).thenReturn(validClaims);
        when(userRepository.findById(42L)).thenReturn(Optional.of(user));

        filter.doFilter(request, response, filterChain);

        assertInstanceOf(UserPrincipal.class, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
        verify(authenticationEntryPoint, never()).commence(any(), any(), any());
    }

    @Test
    void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userRepository, authenticationEntryPoint);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/plans/generate-with-ai");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer bad-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.parseAndValidateAccessToken("bad-token")).thenThrow(new RuntimeException("bad token"));

        filter.doFilter(request, response, filterChain);

        verify(authenticationEntryPoint).commence(any(), any(), any());
        verify(filterChain, never()).doFilter(any(), any());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserNotFound() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userRepository, authenticationEntryPoint);
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/plans/generate-with-ai");
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer token-value");
        MockHttpServletResponse response = new MockHttpServletResponse();

        io.jsonwebtoken.Claims missingUserClaims = io.jsonwebtoken.Jwts.claims();
        missingUserClaims.put("userId", 999L);
        when(jwtService.parseAndValidateAccessToken("token-value")).thenReturn(missingUserClaims);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        filter.doFilter(request, response, filterChain);

        verify(authenticationEntryPoint).commence(any(), any(), any());
        verify(filterChain, never()).doFilter(any(), any());
    }
}
