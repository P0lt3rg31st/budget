package ru.utmn.budget.specdto.imports;

public record ImportRequest(
        String format,
        String payload,
        Boolean dryRun
) {
}
