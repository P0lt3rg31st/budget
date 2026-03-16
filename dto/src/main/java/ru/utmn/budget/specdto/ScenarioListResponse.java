package ru.utmn.budget.specdto;

import java.util.List;

public record ScenarioListResponse(
        List<ScenarioDto> items
) {
}
