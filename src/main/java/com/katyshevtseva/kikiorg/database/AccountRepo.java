package com.katyshevtseva.kikiorg.database;

import com.katyshevtseva.kikiorg.core.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
}
