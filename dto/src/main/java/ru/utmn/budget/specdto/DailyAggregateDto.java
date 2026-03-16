package ru.utmn.budget.specdto;

import java.time.LocalDate;
import java.util.Map;

public record DailyAggregateDto(
        LocalDate date,
        Double expenseTotal,
        Double incomeTotal,
        CurrencyCode currency,
        Map<String, Double> byCategory
) {
}
