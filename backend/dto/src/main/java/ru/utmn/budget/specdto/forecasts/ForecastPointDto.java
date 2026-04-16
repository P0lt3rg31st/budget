package ru.utmn.budget.specdto.forecasts;

import java.time.LocalDate;
import ru.utmn.budget.specdto.common.CurrencyCode;

public record ForecastPointDto(
        LocalDate date,
        CurrencyCode currency,
        Double expenseP50,
        Double expenseP90,
        Double incomeP50,
        Double incomeP90
) {
}
