package ru.utmn.budget.specdto.transactions;

import java.time.OffsetDateTime;
import ru.utmn.budget.specdto.common.Money;

public record TransactionUpdateRequest(
        Long accountId,
        Long categoryId,
        String merchantName,
        String note,
        OffsetDateTime occurredAt,
        Money money
) {
}
