package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record TransactionUpdateRequest(
        Long accountId,
        Long categoryId,
        String merchantName,
        String note,
        OffsetDateTime occurredAt,
        Money money
) {
}
