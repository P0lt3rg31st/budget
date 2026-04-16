package ru.utmn.budget.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import ru.utmn.budget.model.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @EntityGraph(attributePaths = {"account", "category"})
    Optional<Transaction> findByIdAndAccount_User_Id(Long transactionId, Long userId);

    @Override
    @EntityGraph(attributePaths = {"account", "category"})
    Page<Transaction> findAll(@Nullable Specification<Transaction> spec, Pageable pageable);
}