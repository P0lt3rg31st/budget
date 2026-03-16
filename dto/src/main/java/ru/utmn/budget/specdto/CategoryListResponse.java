package ru.utmn.budget.specdto;

import java.util.List;

public record CategoryListResponse(
        List<CategoryDto> items
) {
}
