package ru.utmn.budget.specdto.budgets;

import java.util.List;

public record BudgetPlanListResponse(
        List<BudgetPlanDto> items
) {
}
