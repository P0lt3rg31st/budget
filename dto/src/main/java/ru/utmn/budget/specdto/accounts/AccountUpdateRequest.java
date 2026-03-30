package ru.utmn.budget.specdto.accounts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountUpdateRequest(
        @Size(max = 80)
        @Pattern(regexp = ".*\\S.*", message = "name must not be blank")
        String name,

        @NotNull
        Boolean archived
) {
}