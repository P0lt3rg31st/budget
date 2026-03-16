package ru.utmn.budget.specdto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
