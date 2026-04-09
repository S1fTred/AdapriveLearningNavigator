package com.example.adaprivelearningnavigator.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceImplTest {

    @Test
    void shouldGenerateAndParseAccessToken() {
        JwtProperties props = new JwtProperties(
                "test-secret-key-for-jwt-at-least-32-bytes",
                "aln-test",
                Duration.ofMinutes(15),
                Duration.ofDays(7)
        );
        JwtServiceImpl service = new JwtServiceImpl(props);
        UserPrincipal principal = new UserPrincipal(42L, "test@example.com");

        String token = service.generateAccessToken(principal);
        Claims claims = service.parseAndValidateAccessToken(token);

        assertEquals("aln-test", claims.getIssuer());
        assertEquals("test@example.com", claims.getSubject());
        assertEquals(42L, claims.get("userId", Number.class).longValue());
        assertEquals("access", claims.get("tokenType", String.class));
    }

    @Test
    void shouldGenerateAndParseRefreshToken() {
        JwtProperties props = new JwtProperties(
                "test-secret-key-for-jwt-at-least-32-bytes",
                "aln-test",
                Duration.ofMinutes(15),
                Duration.ofDays(7)
        );
        JwtServiceImpl service = new JwtServiceImpl(props);
        UserPrincipal principal = new UserPrincipal(42L, "test@example.com");

        String token = service.generateRefreshToken(principal);
        Claims claims = service.parseAndValidateRefreshToken(token);

        assertEquals("refresh", claims.get("tokenType", String.class));
    }

    @Test
    void shouldRejectTokenWithWrongIssuer() {
        JwtProperties issuingProps = new JwtProperties(
                "test-secret-key-for-jwt-at-least-32-bytes",
                "issuer-a",
                Duration.ofMinutes(15),
                Duration.ofDays(7)
        );
        JwtProperties validatingProps = new JwtProperties(
                "test-secret-key-for-jwt-at-least-32-bytes",
                "issuer-b",
                Duration.ofMinutes(15),
                Duration.ofDays(7)
        );

        JwtServiceImpl issuingService = new JwtServiceImpl(issuingProps);
        JwtServiceImpl validatingService = new JwtServiceImpl(validatingProps);

        String token = issuingService.generateAccessToken(new UserPrincipal(42L, "test@example.com"));

        assertThrows(Exception.class, () -> validatingService.parseAndValidateAccessToken(token));
    }

    @Test
    void shouldRejectExpiredToken() {
        JwtProperties props = new JwtProperties(
                "test-secret-key-for-jwt-at-least-32-bytes",
                "aln-test",
                Duration.ofSeconds(-1),
                Duration.ofDays(7)
        );
        JwtServiceImpl service = new JwtServiceImpl(props);

        String token = service.generateAccessToken(new UserPrincipal(42L, "test@example.com"));

        assertThrows(ExpiredJwtException.class, () -> service.parseAndValidateAccessToken(token));
    }

    @Test
    void shouldRejectAccessTokenWhenValidatedAsRefresh() {
        JwtProperties props = new JwtProperties(
                "test-secret-key-for-jwt-at-least-32-bytes",
                "aln-test",
                Duration.ofMinutes(15),
                Duration.ofDays(7)
        );
        JwtServiceImpl service = new JwtServiceImpl(props);

        String accessToken = service.generateAccessToken(new UserPrincipal(42L, "test@example.com"));

        assertThrows(JwtException.class, () -> service.parseAndValidateRefreshToken(accessToken));
    }
}
