package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;
import ru.utmn.budget.util.CashflowType;

public record TransactionDto(
        Long id,
        Long accountId,
        CashflowType type,
        Long categoryId,
        String merchantName,
        String note,
        OffsetDateTime occurredAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Money money
) {
}
