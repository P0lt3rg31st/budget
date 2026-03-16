package ru.utmn.budget.specdto;

import java.util.List;
import java.util.Map;

public record ForecastEngineCallback(
        Long runId,
        ForecastRunStatus status,
        Map<String, Double> metrics,
        List<ForecastPointDto> points
) {
}
