package ru.utmn.budget.specdto.alerts;

import java.util.List;

public record AlertListResponse(
        List<AlertDto> items
) {
}
