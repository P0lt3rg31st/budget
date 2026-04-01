package ru.utmn.budget.transaction;

import org.springframework.data.jpa.domain.Specification;
import ru.utmn.budget.model.domain.Transaction;
import ru.utmn.budget.util.CashflowType;

import java.time.Instant;

public final class TransactionSpecifications {

    private TransactionSpecifications() {
    }

    public static Specification<Transaction> ownedByUser(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("account").get("user").get("id"), userId);
    }

    public static Specification<Transaction> hasAccountId(Long accountId) {
        return (root, query, cb) ->
                cb.equal(root.get("account").get("id"), accountId);
    }

    public static Specification<Transaction> hasType(CashflowType type) {
        return (root, query, cb) ->
                cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> hasCategoryId(Long categoryId) {
        return (root, query, cb) ->
                cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Transaction> occurredAtGte(Instant occurredFrom) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("occurredAt"), occurredFrom);
    }

    public static Specification<Transaction> occurredAtLte(Instant occurredTo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("occurredAt"), occurredTo);
    }

    public static Specification<Transaction> withFilters(
            Long userId,
            Long accountId,
            CashflowType type,
            Long categoryId,
            Instant occurredFrom,
            Instant occurredTo
    ) {
        Specification<Transaction> spec = Specification.where(ownedByUser(userId));

        if (accountId != null) {
            spec = spec.and(hasAccountId(accountId));
        }
        if (type != null) {
            spec = spec.and(hasType(type));
        }
        if (categoryId != null) {
            spec = spec.and(hasCategoryId(categoryId));
        }
        if (occurredFrom != null) {
            spec = spec.and(occurredAtGte(occurredFrom));
        }
        if (occurredTo != null) {
            spec = spec.and(occurredAtLte(occurredTo));
        }

        return spec;
    }
}