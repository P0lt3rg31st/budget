package ru.utmn.budget.specdto.accounts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.utmn.budget.specdto.common.CurrencyCode;

public record AccountCreateRequest(
        @NotBlank
        @Size(max = 80)
        String name,

        @NotNull
        @Valid
        CurrencyCode currency
) {
}