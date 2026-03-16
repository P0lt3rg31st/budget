package ru.utmn.budget.specdto.categories;

public record CategoryUpdateRequest(
        String name,
        Boolean archived
) {
}
