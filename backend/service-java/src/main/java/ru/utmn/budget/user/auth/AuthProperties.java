package ru.utmn.budget.user.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "budget.auth")
public record AuthProperties(
        String secret,
        long accessTokenExpiresInSeconds,
        String issuer
) {
}