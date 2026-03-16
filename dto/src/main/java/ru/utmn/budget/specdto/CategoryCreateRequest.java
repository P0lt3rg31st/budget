package ru.utmn.budget.specdto;

import ru.utmn.budget.util.CashflowType;

public record CategoryCreateRequest(
        String name,
        CashflowType type
) {
}
