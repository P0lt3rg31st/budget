package ru.utmn.budget.specdto;

public record Money(
        Double amount,
        CurrencyCode currency
) {
}
