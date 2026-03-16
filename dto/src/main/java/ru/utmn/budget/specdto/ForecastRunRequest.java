package ru.utmn.budget.specdto;

import java.util.List;

public record ForecastRunRequest(
        Integer horizonDays,
        Integer historyDays,
        ForecastModelType modelType,
        Boolean includeIncome,
        List<Double> confidenceLevels
) {
}
