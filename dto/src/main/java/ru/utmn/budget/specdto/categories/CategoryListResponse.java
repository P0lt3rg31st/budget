package ru.utmn.budget.specdto.categories;

import java.util.List;

public record CategoryListResponse(
        List<CategoryDto> items
) {
}
