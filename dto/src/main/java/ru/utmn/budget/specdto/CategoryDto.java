package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record CategoryDto(
        Long id,
        String name,
        FlowType type,
        Boolean archived,
        OffsetDateTime createdAt
) {
}
