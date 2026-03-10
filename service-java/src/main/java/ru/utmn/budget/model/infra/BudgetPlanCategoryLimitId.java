package ru.utmn.budget.model.infra;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BudgetPlanCategoryLimitId implements Serializable {
    private Long plan;
    private Long category;
}