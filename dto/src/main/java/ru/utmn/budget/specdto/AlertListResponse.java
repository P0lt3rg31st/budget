package ru.utmn.budget.specdto;

import java.util.List;

public record AlertListResponse(
        List<AlertDto> items
) {
}
