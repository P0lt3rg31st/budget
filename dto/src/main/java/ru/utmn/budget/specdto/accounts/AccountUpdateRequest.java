package ru.utmn.budget.specdto.accounts;

public record AccountUpdateRequest(
        String name,
        Boolean archived
) {
}
