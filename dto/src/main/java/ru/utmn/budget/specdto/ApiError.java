package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record ApiError(
        Integer status,
        String error,
        String message,
        String path,
        OffsetDateTime timestamp,
        String traceId
) {
}
