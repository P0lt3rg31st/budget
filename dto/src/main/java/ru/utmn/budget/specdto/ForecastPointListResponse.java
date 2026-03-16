package ru.utmn.budget.specdto;

import java.util.List;

public record ForecastPointListResponse(
        List<ForecastPointDto> items
) {
}
