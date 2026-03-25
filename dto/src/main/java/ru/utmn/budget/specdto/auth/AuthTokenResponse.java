package ru.utmn.budget.specdto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthTokenResponse(

        @NotBlank
        String tokenType,

        @NotBlank
        String accessToken,

        @NotNull
        Long expiresIn
) {
}