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
@RequestMapping("/users/{userId}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @PathVariable Long userId,
            @RequestParam(required = false) CashflowType type,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "20") @Positive int size
    ) {
        return categoryService.getCategories(userId, type, from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(
            @PathVariable Long userId,
            @PathVariable Long categoryId
    ) {
        return categoryService.getCategoryById(userId, categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @PathVariable Long userId,
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        return categoryService.createCategory(userId, request);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        return categoryService.updateCategory(userId, categoryId, request);
    }
}