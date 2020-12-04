package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    List<Expense> findByAccount(Account account);

    List<Expense> findByItemAndDateEntityAndAccount(Item item, DateEntity dateEntity, Account account);
}
