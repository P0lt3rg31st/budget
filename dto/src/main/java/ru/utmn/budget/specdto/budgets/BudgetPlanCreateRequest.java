package ru.utmn.budget.specdto.budgets;

import java.time.LocalDate;
import java.util.Map;
import ru.utmn.budget.specdto.common.CurrencyCode;

public record BudgetPlanCreateRequest(
        LocalDate periodStart,
        LocalDate periodEnd,
        CurrencyCode currency,
        Double totalExpenseLimit,
        Map<String, Double> categoryLimits
) {
}
