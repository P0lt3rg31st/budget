package ru.utmn.budget.category;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.utmn.budget.util.CashflowType;
import ru.utmn.budget.model.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndUser_Id(Long categoryId, Long userId);

    List<Category> findAllByUser_Id(Long userId, Pageable pageable);

    List<Category> findAllByUser_IdAndType(Long userId, CashflowType type, Pageable pageable);

    boolean existsByUser_IdAndTypeAndNameIgnoreCase(
            Long userId,
            CashflowType type,
            String name
    );

    boolean existsByUser_IdAndTypeAndNameIgnoreCaseAndIdNot(
            Long userId,
            CashflowType type,
            String name,
            Long categoryId
    );

}