package ru.utmn.budget.specdto;

public record AccountCreateRequest(
        String name,
        CurrencyCode currency
) {
}
