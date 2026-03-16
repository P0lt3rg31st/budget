package ru.utmn.budget.specdto.alerts;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import ru.utmn.budget.alert.AlertSeverity;
import ru.utmn.budget.alert.AlertStatus;
import ru.utmn.budget.alert.AlertType;

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
