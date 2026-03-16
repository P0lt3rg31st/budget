package ru.utmn.budget.specdto.forecasts;

import java.util.List;

public record ForecastPointListResponse(
        List<ForecastPointDto> items
) {
}
