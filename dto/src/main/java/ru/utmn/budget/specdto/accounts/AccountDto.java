package ru.utmn.budget.specdto.accounts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.utmn.budget.specdto.common.CurrencyCode;

import java.time.OffsetDateTime;

public record AccountDto(
        @NotNull
        @Positive
        Long id,

        @NotBlank
        @Size(max = 80)
        String name,

        @NotNull
        @Valid
        CurrencyCode currency,

        @NotNull
        Boolean archived,

        @NotNull
        OffsetDateTime createdAt
) {
}