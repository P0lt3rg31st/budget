package ru.utmn.budget.specdto;

import java.util.List;

public record AccountListResponse(
        List<AccountDto> items
) {
}
