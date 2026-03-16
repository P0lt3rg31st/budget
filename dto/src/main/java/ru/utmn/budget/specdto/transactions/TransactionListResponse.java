package ru.utmn.budget.specdto.transactions;

import java.util.List;

public record TransactionListResponse(
        List<TransactionDto> items
) {
}
