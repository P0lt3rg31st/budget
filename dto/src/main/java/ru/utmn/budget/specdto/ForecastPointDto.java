package ru.utmn.budget.specdto;

import java.time.LocalDate;

public record ForecastPointDto(
        LocalDate date,
        CurrencyCode currency,
        Double expenseP50,
        Double expenseP90,
        Double incomeP50,
        Double incomeP90
) {
}
