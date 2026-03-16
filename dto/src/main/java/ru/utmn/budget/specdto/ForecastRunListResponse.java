package ru.utmn.budget.specdto;

import java.util.List;

public record ForecastRunListResponse(
        List<ForecastRunDto> items
) {
}
