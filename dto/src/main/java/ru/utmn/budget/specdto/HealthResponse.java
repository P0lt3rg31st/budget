package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record HealthResponse(
        String status,
        OffsetDateTime time
) {
}
