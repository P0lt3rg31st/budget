package ru.utmn.budget.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.utmn.budget.specdto.categories.CategoryCreateRequest;
import ru.utmn.budget.specdto.categories.CategoryDto;
import ru.utmn.budget.specdto.categories.CategoryUpdateRequest;
import ru.utmn.budget.util.CashflowType;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(name = "type", required = false) CashflowType type,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size
    ) {
        return categoryService.getCategories(type, from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return categoryService.createCategory(request);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        return categoryService.updateCategory(categoryId, request);
    }
}