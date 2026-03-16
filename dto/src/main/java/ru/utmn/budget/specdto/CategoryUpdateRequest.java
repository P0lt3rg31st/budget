package ru.utmn.budget.specdto;

public record CategoryUpdateRequest(
        String name,
        Boolean archived
) {
}
