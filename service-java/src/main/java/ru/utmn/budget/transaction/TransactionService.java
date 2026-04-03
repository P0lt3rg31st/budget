package ru.utmn.budget.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.utmn.budget.OffsetPageRequest;
import ru.utmn.budget.account.AccountRepository;
import ru.utmn.budget.category.CategoryRepository;
import ru.utmn.budget.handler.BadRequestException;
import ru.utmn.budget.handler.ConflictException;
import ru.utmn.budget.handler.NotFoundException;
import ru.utmn.budget.model.domain.Account;
import ru.utmn.budget.model.domain.Category;
import ru.utmn.budget.model.domain.Transaction;
import ru.utmn.budget.specdto.transactions.TransactionCreateRequest;
import ru.utmn.budget.specdto.transactions.TransactionDto;
import ru.utmn.budget.specdto.transactions.TransactionListResponse;
import ru.utmn.budget.specdto.transactions.TransactionUpdateRequest;
import ru.utmn.budget.util.CashflowType;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private static final Sort TRANSACTION_SORT = Sort.by(
            Sort.Order.desc("occurredAt"),
            Sort.Order.desc("id")
    );

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    public TransactionListResponse getTransactions(
            Long userId,
            Long accountId,
            CashflowType type,
            Long categoryId,
            Instant occurredFrom,
            Instant occurredTo,
            int from,
            int size
    ) {
        validateDateRange(occurredFrom, occurredTo);

        OffsetPageRequest pageRequest = OffsetPageRequest.of(from, size, TRANSACTION_SORT);

        List<TransactionDto> items = transactionRepository.findAll(
                        TransactionSpecifications.withFilters(
                                userId,
                                accountId,
                                type,
                                categoryId,
                                occurredFrom,
                                occurredTo
                        ),
                        pageRequest
                )
                .getContent()
                .stream()
                .map(transactionMapper::toDto)
                .toList();

        return new TransactionListResponse(items);
    }

    public TransactionDto getTransaction(Long userId, Long transactionId) {
        Transaction transaction = getOwnedTransactionOrThrow(transactionId, userId);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionDto create(Long userId, TransactionCreateRequest request) {
        Account account = getOwnedAccountOrThrow(request.accountId(), userId);
        Category category = getOwnedCategoryOrThrow(request.categoryId(), userId);

        validateCategoryMatchesType(category, request.type());
        validateActiveAccount(account);
        validateActiveCategory(category);

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toDto(saved);
    }

    @Transactional
    public TransactionDto update(Long userId, Long transactionId, TransactionUpdateRequest request) {
        validatePatchHasChanges(request);

        Transaction transaction = getOwnedTransactionOrThrow(transactionId, userId);

        Category category = transaction.getCategory();
        CashflowType resultingType = request.type() != null ? request.type() : transaction.getType();

        if (request.categoryId() != null) {
            category = getOwnedCategoryOrThrow(request.categoryId(), userId);
            validateActiveCategory(category);
        }

        validateCategoryMatchesType(category, resultingType);

        transactionMapper.updateEntityFromRequest(request, transaction);

        if (request.categoryId() != null) {
            transaction.setCategory(category);
        }

        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long userId, Long transactionId) {
        Transaction transaction = getOwnedTransactionOrThrow(transactionId, userId);
        transactionRepository.delete(transaction);
    }

    private Transaction getOwnedTransactionOrThrow(Long transactionId, Long userId) {
        return transactionRepository.findByIdAndAccount_User_Id(transactionId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Transaction with id=" + transactionId + " not found"
                ));
    }

    private Account getOwnedAccountOrThrow(Long accountId, Long userId) {
        return accountRepository.findByIdAndUser_IdAndArchivedFalse(accountId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Account with id=" + accountId + " not found"
                ));
    }

    private Category getOwnedCategoryOrThrow(Long categoryId, Long userId) {
        return categoryRepository.findByIdAndUser_IdAndArchivedFalse(categoryId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Category with id=" + categoryId + " not found"
                ));
    }

    private void validateCategoryMatchesType(Category category, CashflowType type) {
        if (category.getType() != type) {
            throw new BadRequestException(
                    "Category type does not match transaction type"
            );
        }
    }

    private void validateDateRange(Instant occurredFrom, Instant occurredTo) {
        if (occurredFrom != null && occurredTo != null && occurredFrom.isAfter(occurredTo)) {
            throw new BadRequestException("occurredFrom must be <= occurredTo");
        }
    }

    private void validatePatchHasChanges(TransactionUpdateRequest request) {
        if (request.type() == null
                && request.categoryId() == null
                && request.counterpartyName() == null
                && request.note() == null
                && request.occurredAt() == null
                && request.amount() == null) {
            throw new BadRequestException("Patch request must contain at least one field");
        }
    }

    private void validateActiveAccount(Account account) {
        if (account.isArchived()) {
            throw new BadRequestException("Archived account cannot be used for new transactions");
        }
    }

    private void validateActiveCategory(Category category) {
        if (category.isArchived()) {
            throw new BadRequestException("Archived category cannot be used for new transactions");
        }
    }
}