package ru.utmn.budget.specdto.accounts;

import java.time.OffsetDateTime;
import ru.utmn.budget.specdto.common.CurrencyCode;

public record AccountDto(
        Long id,
        String name,
        CurrencyCode currency,
        Boolean archived,
        OffsetDateTime createdAt
) {
}
