package ru.utmn.budget.specdto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;

public record BudgetPlanDto(
        Long id,
        LocalDate periodStart,
        LocalDate periodEnd,
        CurrencyCode currency,
        Double totalExpenseLimit,
        Map<String, Double> categoryLimits,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
