package ru.utmn.budget.specdto;

public record LoginRequest(
        String email,
        String password
) {
}
