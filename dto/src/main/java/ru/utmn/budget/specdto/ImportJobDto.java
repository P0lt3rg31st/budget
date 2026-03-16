package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record ImportJobDto(
        String jobId,
        String status,
        OffsetDateTime createdAt,
        String errorMessage
) {
}
