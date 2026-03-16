package ru.utmn.budget.specdto;

public record CategoryCreateRequest(
        String name,
        FlowType type
) {
}
