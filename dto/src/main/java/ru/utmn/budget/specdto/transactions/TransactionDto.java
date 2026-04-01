package ru.utmn.budget.specdto.transactions;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.utmn.budget.util.CashflowType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionDto(
        @NotNull
        @Positive
        Long id,

        @NotNull
        @Positive
        Long accountId,

        @NotNull
        CashflowType type,

        @NotNull
        @Positive
        Long categoryId,

        @Size(max = 120)
        @Pattern(regexp = ".*\\S.*", message = "counterpartyName must not be blank")
        String counterpartyName,

        @Size(max = 500)
        @Pattern(regexp = ".*\\S.*", message = "note must not be blank")
        String note,

        @NotNull
        OffsetDateTime occurredAt,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 11, fraction = 8)
        BigDecimal amount,

        @NotNull
        OffsetDateTime createdAt,

        OffsetDateTime updatedAt
) {
}