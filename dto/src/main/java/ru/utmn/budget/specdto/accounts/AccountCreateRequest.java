package ru.utmn.budget.specdto.accounts;

import ru.utmn.budget.specdto.common.CurrencyCode;

public record AccountCreateRequest(
        String name,
        CurrencyCode currency
) {
}
