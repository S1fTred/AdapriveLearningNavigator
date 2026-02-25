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

    private final JwtProperties props;

    private Key key() {
        // Для HS256 секрет должен быть достаточно длинным
        return Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.ttlSeconds());

        return Jwts.builder()
                .setIssuer(props.issuer())
                .setSubject(principal.getUsername()) // email
                .claim("userId", principal.getUserId())
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
}
