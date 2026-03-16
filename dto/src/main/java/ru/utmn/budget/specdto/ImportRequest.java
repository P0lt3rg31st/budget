package ru.utmn.budget.specdto;

public record ImportRequest(
        String format,
        String payload,
        Boolean dryRun
) {
}
