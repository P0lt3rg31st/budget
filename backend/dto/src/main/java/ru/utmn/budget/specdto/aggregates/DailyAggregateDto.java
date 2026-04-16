package ru.utmn.budget.specdto.aggregates;

import java.time.LocalDate;
import java.util.Map;
import ru.utmn.budget.specdto.common.CurrencyCode;

public record DailyAggregateDto(
        LocalDate date,
        Double expenseTotal,
        Double incomeTotal,
        CurrencyCode currency,
        Map<String, Double> byCategory
) {
}
