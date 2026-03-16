package ru.utmn.budget.specdto;

import java.util.Map;

public record BudgetPlanUpdateRequest(
        Double totalExpenseLimit,
        Map<String, Double> categoryLimits
) {
}
