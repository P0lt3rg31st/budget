package ru.utmn.budget.specdto;

import ru.utmn.budget.alert.AlertStatus;

public record AlertUpdateRequest(
        AlertStatus status
) {
}
