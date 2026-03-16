package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ScenarioDto(
        Long id,
        String name,
        ScenarioType type,
        Map<String, Object> parameters,
        Long resultForecastRunId,
        OffsetDateTime createdAt
) {
}
