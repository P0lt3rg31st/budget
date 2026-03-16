package ru.utmn.budget.specdto.auth;

public record AuthTokenResponse(
        String tokenType,
        String accessToken,
        Long expiresIn
) {
}
