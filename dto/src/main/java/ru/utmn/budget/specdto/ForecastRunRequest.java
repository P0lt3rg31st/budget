package ru.utmn.budget.specdto;

import java.util.List;
import ru.utmn.budget.forecast.ForecastModelType;

public record ForecastRunRequest(
        Integer horizonDays,
        Integer historyDays,
        ForecastModelType modelType,
        Boolean includeIncome,
        List<Double> confidenceLevels
) {
}
