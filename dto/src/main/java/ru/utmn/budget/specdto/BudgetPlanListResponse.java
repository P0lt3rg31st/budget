package ru.utmn.budget.specdto;

import java.util.List;

public record BudgetPlanListResponse(
        List<BudgetPlanDto> items
) {
}
