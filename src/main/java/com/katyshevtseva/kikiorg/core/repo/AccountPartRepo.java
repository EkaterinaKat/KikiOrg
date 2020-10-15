package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.finance.entity.AccountPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountPartRepo extends JpaRepository<AccountPart, Long> {
}
