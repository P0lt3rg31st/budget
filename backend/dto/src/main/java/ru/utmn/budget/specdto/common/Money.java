package ru.utmn.budget.specdto.common;

public record Money(
        Double amount,
        CurrencyCode currency
) {
}
