package ru.utmn.budget.specdto;

import java.util.List;

public record DailyAggregateListResponse(
        List<DailyAggregateDto> items
) {
}
