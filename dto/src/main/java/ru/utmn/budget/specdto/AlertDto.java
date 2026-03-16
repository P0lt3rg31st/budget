package ru.utmn.budget.specdto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AlertDto(
        Long id,
        AlertType type,
        AlertSeverity severity,
        AlertStatus status,
        LocalDate date,
        String message,
        Long relatedForecastRunId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
