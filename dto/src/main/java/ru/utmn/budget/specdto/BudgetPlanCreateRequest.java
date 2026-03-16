package ru.utmn.budget.specdto;

import java.time.LocalDate;
import java.util.Map;

public record BudgetPlanCreateRequest(
        LocalDate periodStart,
        LocalDate periodEnd,
        CurrencyCode currency,
        Double totalExpenseLimit,
        Map<String, Double> categoryLimits
) {
}
