package com.example.adaprivelearningnavigator.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final JwtProperties props;

    private Key key() {
        // Для HS256 секрет должен быть достаточно длинным
        return Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(UserPrincipal principal) {
        return generateAccessToken(principal);
    }

    @Override
    public String generateAccessToken(UserPrincipal principal) {
        return generateToken(principal, props.accessTtl().getSeconds(), ACCESS_TOKEN_TYPE);
    }

    @Override
    public String generateRefreshToken(UserPrincipal principal) {
        return generateToken(principal, props.refreshTtl().getSeconds(), REFRESH_TOKEN_TYPE);
    }

    private String generateToken(UserPrincipal principal, long ttlSeconds, String tokenType) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);

        return Jwts.builder()
                .setIssuer(props.issuer())
                .setSubject(principal.getUsername())
                .claim("userId", principal.getUserId())
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims parseAndValidate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .requireIssuer(props.issuer())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Claims parseAndValidateAccessToken(String token) {
        Claims claims = parseAndValidate(token);
        validateTokenType(claims, ACCESS_TOKEN_TYPE);
        return claims;
    }

    @Override
    public Claims parseAndValidateRefreshToken(String token) {
        Claims claims = parseAndValidate(token);
        validateTokenType(claims, REFRESH_TOKEN_TYPE);
        return claims;
    }

    private void validateTokenType(Claims claims, String expectedType) {
        String actualType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (!expectedType.equals(actualType)) {
            throw new JwtException("Invalid token type");
        }
    }
}
