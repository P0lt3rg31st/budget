package ru.utmn.budget.specdto.forecasts;

import java.util.List;

public record ForecastRunListResponse(
        List<ForecastRunDto> items
) {
}
