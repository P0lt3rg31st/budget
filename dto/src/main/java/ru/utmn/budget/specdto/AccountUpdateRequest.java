package ru.utmn.budget.specdto;

public record AccountUpdateRequest(
        String name,
        Boolean archived
) {
}
