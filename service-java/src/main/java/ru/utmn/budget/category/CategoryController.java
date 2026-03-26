package ru.utmn.budget.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "type", required = false) CashflowType type,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size
    ) {
        Long userId = extractUserId(jwt);
        return categoryService.getCategories(userId, type, from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long categoryId
    ) {
        Long userId = extractUserId(jwt);
        return categoryService.getCategoryById(userId, categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CategoryCreateRequest request
    ) {
        Long userId = extractUserId(jwt);
        return categoryService.createCategory(userId, request);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto updateCategory(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        Long userId = extractUserId(jwt);
        return categoryService.updateCategory(userId, categoryId, request);
    }

    private Long extractUserId(Jwt jwt) {
        return ((Number) jwt.getClaim("user_id")).longValue();
    }
}