package ru.utmn.budget.specdto.transactions;

import java.time.OffsetDateTime;
import ru.utmn.budget.specdto.common.Money;
import ru.utmn.budget.util.CashflowType;

public record TransactionCreateRequest(
        Long accountId,
        CashflowType type,
        Long categoryId,
        String merchantName,
        String note,
        OffsetDateTime occurredAt,
        Money money
) {
}
