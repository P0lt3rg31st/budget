package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;
import java.util.Map;

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
