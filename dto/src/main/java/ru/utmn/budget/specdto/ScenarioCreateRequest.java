package ru.utmn.budget.specdto;

import java.util.Map;

public record ScenarioCreateRequest(
        String name,
        ScenarioType type,
        Map<String, Object> parameters,
        Boolean runForecast
) {
}
