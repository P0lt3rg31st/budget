package ru.utmn.budget.specdto.users;

import java.time.OffsetDateTime;

public record UserDto(
        Long id,
        String email,
        String displayName,
        OffsetDateTime createdAt
) {
}
