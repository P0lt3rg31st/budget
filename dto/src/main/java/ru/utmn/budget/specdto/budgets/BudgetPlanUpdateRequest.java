package ru.utmn.budget.specdto.budgets;

import java.util.Map;

public record BudgetPlanUpdateRequest(
        Double totalExpenseLimit,
        Map<String, Double> categoryLimits
) {
}
