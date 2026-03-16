package ru.utmn.budget.specdto.categories;

import java.time.OffsetDateTime;
import ru.utmn.budget.util.CashflowType;

public record CategoryDto(
        Long id,
        String name,
        CashflowType type,
        Boolean archived,
        OffsetDateTime createdAt
) {
}
