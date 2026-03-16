package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record TransactionDto(
        Long id,
        Long accountId,
        FlowType type,
        Long categoryId,
        String merchantName,
        String note,
        OffsetDateTime occurredAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Money money
) {
}
