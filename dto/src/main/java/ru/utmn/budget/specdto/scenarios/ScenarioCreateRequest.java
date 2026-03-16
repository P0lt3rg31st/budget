package ru.utmn.budget.specdto.scenarios;

import java.util.Map;
import ru.utmn.budget.util.ScenarioType;

public record ScenarioCreateRequest(
        String name,
        ScenarioType type,
        Map<String, Object> parameters,
        Boolean runForecast
) {
}
