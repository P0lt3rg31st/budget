package ru.utmn.budget.specdto;

import java.time.OffsetDateTime;

public record UserDto(
        Long id,
        String email,
        String displayName,
        OffsetDateTime createdAt
) {
}
