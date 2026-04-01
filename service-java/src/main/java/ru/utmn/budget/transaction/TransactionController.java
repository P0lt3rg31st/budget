package ru.utmn.budget.transaction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.utmn.budget.specdto.transactions.TransactionCreateRequest;
import ru.utmn.budget.specdto.transactions.TransactionDto;
import ru.utmn.budget.specdto.transactions.TransactionListResponse;
import ru.utmn.budget.specdto.transactions.TransactionUpdateRequest;
import ru.utmn.budget.util.CashflowType;

import java.time.Instant;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public TransactionListResponse getTransactions(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "accountId", required = false) @Positive Long accountId,
            @RequestParam(name = "type", required = false) CashflowType type,
            @RequestParam(name = "categoryId", required = false) @Positive Long categoryId,
            @RequestParam(name = "occurredFrom", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant occurredFrom,
            @RequestParam(name = "occurredTo", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant occurredTo,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive @Max(200) int size
    ) {
        Long userId = extractUserId(jwt);

        return transactionService.getTransactions(
                userId,
                accountId,
                type,
                categoryId,
                occurredFrom,
                occurredTo,
                from,
                size
        );
    }

    @GetMapping("/{transactionId}")
    public TransactionDto getTransaction(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable @Positive Long transactionId
    ) {
        Long userId = extractUserId(jwt);
        return transactionService.getTransaction(userId, transactionId);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody TransactionCreateRequest request
    ) {
        Long userId = extractUserId(jwt);
        TransactionDto created = transactionService.create(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PatchMapping("/{transactionId}")
    public TransactionDto update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable @Positive Long transactionId,
            @Valid @RequestBody TransactionUpdateRequest request
    ) {
        Long userId = extractUserId(jwt);
        return transactionService.update(userId, transactionId, request);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable @Positive Long transactionId
    ) {
        Long userId = extractUserId(jwt);
        transactionService.delete(userId, transactionId);

        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Jwt jwt) {
        return ((Number) jwt.getClaim("user_id")).longValue();
    }
}