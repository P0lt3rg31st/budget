package ru.utmn.budget.specdto.imports;

import java.time.OffsetDateTime;

public record ImportJobDto(
        String jobId,
        String status,
        OffsetDateTime createdAt,
        String errorMessage
) {
}
