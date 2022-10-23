package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.CheckLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckLineRepo extends JpaRepository<CheckLine, Long> {
    List<CheckLine> findAllByAccount(Account account);
}
