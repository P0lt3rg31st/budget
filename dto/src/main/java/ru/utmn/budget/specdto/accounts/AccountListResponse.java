package ru.utmn.budget.specdto.accounts;

import java.util.List;

public record AccountListResponse(
        List<AccountDto> items
) {
}
