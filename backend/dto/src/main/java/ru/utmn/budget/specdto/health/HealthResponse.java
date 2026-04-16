package ru.utmn.budget.specdto.health;

import java.time.OffsetDateTime;

public record HealthResponse(
        String status,
        OffsetDateTime time
) {
}
