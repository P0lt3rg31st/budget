package ru.utmn.budget.specdto.scenarios;

import java.util.List;

public record ScenarioListResponse(
        List<ScenarioDto> items
) {
}
