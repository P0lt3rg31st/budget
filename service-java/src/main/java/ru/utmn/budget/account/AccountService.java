package ru.utmn.budget.account;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.utmn.budget.OffsetPageRequest;
import ru.utmn.budget.handler.BadRequestException;
import ru.utmn.budget.handler.NotFoundException;
import ru.utmn.budget.model.domain.Account;
import ru.utmn.budget.model.domain.User;
import ru.utmn.budget.specdto.common.CurrencyCode;
import ru.utmn.budget.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private static final Sort ACCOUNT_SORT = Sort.by(
            Sort.Order.desc("createdAt"),
            Sort.Order.desc("id")
    );

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public Account create(Long userId, String name, CurrencyCode currency) {
        User user = findUserById(userId);

        Account account = new Account();
        account.setUser(user);
        account.setName(name);
        account.setCurrency(currency.value());

        return accountRepository.save(account);
    }

    public List<Account> findAllByUserId(Long userId, int from, int size) {
        findUserById(userId);

        OffsetPageRequest pageRequest = OffsetPageRequest.of(from, size, ACCOUNT_SORT);
        return accountRepository.findAllByUser_IdAndArchivedFalse(userId, pageRequest);
    }

    public Account findById(Long userId, Long accountId) {
        return findOwnedAccountById(userId, accountId);
    }

    @Transactional
    public Account update(Long userId, Long accountId, String name, Boolean archived) {
        if (name == null && archived == null) {
            throw new BadRequestException("At least one field must be provided for update");
        }

        Account account = findOwnedAccountById(userId, accountId);

        boolean changed = false;

        if (name != null && !name.equals(account.getName())) {
            account.setName(name);
            changed = true;
        }

        if (archived != null && archived != account.isArchived()) {
            account.setArchived(archived);
            changed = true;
        }

        if (!changed) {
            return account;
        }

        return accountRepository.save(account);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Account findOwnedAccountById(Long userId, Long accountId) {
        return accountRepository.findByIdAndUser_IdAndArchivedFalse(accountId, userId)
                .orElseThrow(() -> new NotFoundException("Account with id=" + accountId + " was not found"));
    }
}