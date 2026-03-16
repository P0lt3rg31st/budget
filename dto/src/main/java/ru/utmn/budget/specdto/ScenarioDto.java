package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;
import java.util.Map;
import ru.utmn.budget.util.ScenarioType;

public record ScenarioDto(
        Long id,
        String name,
        ScenarioType type,
        Map<String, Object> parameters,
        Long resultForecastRunId,
        OffsetDateTime createdAt
) {
}
