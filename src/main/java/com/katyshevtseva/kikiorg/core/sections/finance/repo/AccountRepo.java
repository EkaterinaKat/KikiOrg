package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.Currency;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {
    List<Account> findAllByArchivedFalse();

    List<Account> findAllByCurrency(Currency currency);
}
