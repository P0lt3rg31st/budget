package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record AccountDto(
        Long id,
        String name,
        CurrencyCode currency,
        Boolean archived,
        OffsetDateTime createdAt
) {
}
