package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    List<Expense> findByAccountId(Long accountId);
    List<Expense> findAllByOwner(Owner owner);
}
