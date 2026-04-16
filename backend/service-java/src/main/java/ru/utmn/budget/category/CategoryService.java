package ru.utmn.budget.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.utmn.budget.OffsetPageRequest;
import ru.utmn.budget.handler.ConflictException;
import ru.utmn.budget.handler.NotFoundException;
import ru.utmn.budget.model.domain.Category;
import ru.utmn.budget.model.domain.User;
import ru.utmn.budget.specdto.categories.CategoryCreateRequest;
import ru.utmn.budget.specdto.categories.CategoryDto;
import ru.utmn.budget.specdto.categories.CategoryUpdateRequest;
import ru.utmn.budget.user.UserRepository;
import ru.utmn.budget.util.CashflowType;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private static final Sort CATEGORY_SORT = Sort.by(
            Sort.Order.desc("createdAt"),
            Sort.Order.desc("id")
    );

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getCategories(Long userId, CashflowType type, int from, int size) {
        OffsetPageRequest pageable = buildPageable(from, size);

        List<Category> categories = (type == null)
                ? categoryRepository.findAllByUser_IdAndArchivedFalse(userId, pageable)
                : categoryRepository.findAllByUser_IdAndTypeAndArchivedFalse(userId, type, pageable);

        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryDto getCategoryById(Long userId, Long categoryId) {
        Category category = getOwnedCategory(userId, categoryId);
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryDto createCategory(Long userId, CategoryCreateRequest request) {
        String normalizedName = normalizeName(request.name());

        assertCategoryNameUniqueForCreate(userId, request.type(), normalizedName);

        User user = getUserOrThrow(userId);

        Category category = new Category();
        category.setUser(user);
        category.setName(normalizedName);
        category.setType(request.type());
        category.setArchived(false);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public CategoryDto updateCategory(Long userId, Long categoryId, CategoryUpdateRequest request) {
        Category category = getOwnedCategory(userId, categoryId);

        updateNameIfPresent(category, request);
        updateArchivedIfPresent(category, request);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    private OffsetPageRequest buildPageable(int from, int size) {
        validatePagination(from, size);
        return OffsetPageRequest.of(from, size, CATEGORY_SORT);
    }

    private void validatePagination(int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("Parameter 'from' must be greater than or equal to 0");
        }
        if (size < 1 || size > 200) {
            throw new IllegalArgumentException("Parameter 'size' must be in range 1..200");
        }
    }

    private Category getOwnedCategory(Long userId, Long categoryId) {
        return categoryRepository.findByIdAndUser_IdAndArchivedFalse(categoryId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Category with id=%d was not found".formatted(categoryId)
                ));
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "User with id=%d was not found".formatted(userId)
                ));
    }

    private void assertCategoryNameUniqueForCreate(Long userId, CashflowType type, String name) {
        boolean alreadyExists = categoryRepository.existsByUser_IdAndTypeAndNameIgnoreCase(
                userId,
                type,
                name
        );

        if (alreadyExists) {
            throw new ConflictException(
                    "Category with name='%s' and type='%s' already exists"
                            .formatted(name, type)
            );
        }
    }

    private void assertCategoryNameUniqueForUpdate(Long userId, Long categoryId, CashflowType type, String name) {
        boolean alreadyExists = categoryRepository.existsByUser_IdAndTypeAndNameIgnoreCaseAndIdNot(
                userId,
                type,
                name,
                categoryId
        );

        if (alreadyExists) {
            throw new ConflictException(
                    "Category with name='%s' and type='%s' already exists"
                            .formatted(name, type)
            );
        }
    }

    private void updateNameIfPresent(Category category, CategoryUpdateRequest request) {
        if (request.name() == null) {
            return;
        }

        String normalizedName = normalizeName(request.name());

        if (normalizedName.equalsIgnoreCase(category.getName())) {
            category.setName(normalizedName);
            return;
        }

        assertCategoryNameUniqueForUpdate(
                category.getUser().getId(),
                category.getId(),
                category.getType(),
                normalizedName
        );

        category.setName(normalizedName);
    }

    private void updateArchivedIfPresent(Category category, CategoryUpdateRequest request) {
        if (request.archived() != null) {
            category.setArchived(request.archived());
        }
    }

    private String normalizeName(String rawName) {
        return rawName == null ? null : rawName.trim().replaceAll("\\s+", " ");
    }
}