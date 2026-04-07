package com.example.adaprivelearningnavigator.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret,
        String issuer,
        Duration accessTtl,
        Duration refreshTtl
) {
    public JwtProperties {
        if (issuer == null || issuer.isBlank()) {
            issuer = "aln";
        }
        if (accessTtl == null) {
            accessTtl = Duration.ofMinutes(15);
        }
        if (refreshTtl == null) {
            refreshTtl = Duration.ofDays(7);
        }
    }

    public long ttlSeconds() {
        return accessTtl.getSeconds();
    }
}
