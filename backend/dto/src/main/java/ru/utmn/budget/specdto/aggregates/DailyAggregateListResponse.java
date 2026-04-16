package ru.utmn.budget.specdto.aggregates;

import java.util.List;

public record DailyAggregateListResponse(
        List<DailyAggregateDto> items
) {
}
