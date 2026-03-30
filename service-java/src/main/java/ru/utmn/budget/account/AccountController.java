package ru.utmn.budget.account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.utmn.budget.model.domain.Account;
import ru.utmn.budget.specdto.accounts.AccountCreateRequest;
import ru.utmn.budget.specdto.accounts.AccountDto;
import ru.utmn.budget.specdto.accounts.AccountListResponse;
import ru.utmn.budget.specdto.accounts.AccountUpdateRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<AccountListResponse> getAccounts(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size
    ) {
        Long userId = extractUserId(jwt);

        List<AccountDto> items = accountService.findAllByUserId(userId, from, size).stream()
                .map(accountMapper::toDto)
                .toList();

        return ResponseEntity.ok(new AccountListResponse(items));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable @Positive Long accountId
    ) {
        Long userId = extractUserId(jwt);
        Account account = accountService.findById(userId, accountId);

        return ResponseEntity.ok(accountMapper.toDto(account));
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AccountCreateRequest request
    ) {
        Long userId = extractUserId(jwt);

        Account createdAccount = accountService.create(
                userId,
                request.name(),
                request.currency()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountMapper.toDto(createdAccount));
    }

    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable @Positive Long accountId,
            @Valid @RequestBody AccountUpdateRequest request
    ) {
        Long userId = extractUserId(jwt);

        Account updatedAccount = accountService.update(
                userId,
                accountId,
                request.name(),
                request.archived()
        );

        return ResponseEntity.ok(accountMapper.toDto(updatedAccount));
    }

    private Long extractUserId(Jwt jwt) {
        String subject = jwt.getSubject();

        if (subject == null || subject.isBlank()) {
            throw new IllegalStateException("JWT does not contain subject");
        }

        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("JWT subject contains invalid user id: " + subject, e);
        }
    }
}