package ru.utmn.budget.specdto.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CurrencyCode(
        @NotBlank
        @Size(min = 3, max = 3)
        @Pattern(regexp = "^[A-Z]{3}$")
        String value
) {
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public CurrencyCode {
    }

    @JsonValue
    public String value() {
        return value;
    }
}