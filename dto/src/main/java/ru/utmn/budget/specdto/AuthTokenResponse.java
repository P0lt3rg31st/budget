package ru.utmn.budget.specdto;

public record AuthTokenResponse(
        String tokenType,
        String accessToken,
        Long expiresIn
) {
}
