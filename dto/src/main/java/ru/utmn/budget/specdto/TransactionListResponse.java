package ru.utmn.budget.specdto;

import java.util.List;

public record TransactionListResponse(
        List<TransactionDto> items
) {
}
