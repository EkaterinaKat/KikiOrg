package com.katyshevtseva.kikiorg.database;

import com.katyshevtseva.kikiorg.core.finance.entity.AccountPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountPartRepo extends JpaRepository<AccountPart, Long> {
}
