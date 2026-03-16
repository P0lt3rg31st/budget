package ru.utmn.budget.specdto.alerts;

import ru.utmn.budget.alert.AlertStatus;

public record AlertUpdateRequest(
        AlertStatus status
) {
}
