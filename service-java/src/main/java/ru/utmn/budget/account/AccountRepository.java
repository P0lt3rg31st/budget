package ru.utmn.budget.account;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.utmn.budget.model.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUser_Id(Long userId, Pageable pageable);

    Optional<Account> findByIdAndUser_Id(Long accountId, Long userId);
}