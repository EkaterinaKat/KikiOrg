package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.PotentialExpense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotentialExpenseRepo extends JpaRepository<PotentialExpense, Long> {
}
