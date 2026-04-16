package ru.utmn.budget.specdto.forecasts;

import java.time.OffsetDateTime;
import java.util.Map;
import ru.utmn.budget.forecast.ForecastModelType;
import ru.utmn.budget.forecast.ForecastRunStatus;

public record ForecastRunDto(
        Long id,
        ForecastRunStatus status,
        ForecastModelType modelType,
        Integer horizonDays,
        Integer historyDays,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        Map<String, Double> metrics,
        String errorMessage
) {
}
