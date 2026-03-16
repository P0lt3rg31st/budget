package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record TransactionCreateRequest(
        Long accountId,
        FlowType type,
        Long categoryId,
        String merchantName,
        String note,
        OffsetDateTime occurredAt,
        Money money
) {
}
