package ru.utmn.budget.specdto.forecasts;

import java.util.List;
import java.util.Map;
import ru.utmn.budget.forecast.ForecastRunStatus;

public record ForecastEngineCallback(
        Long runId,
        ForecastRunStatus status,
        Map<String, Double> metrics,
        List<ForecastPointDto> points
) {
}
