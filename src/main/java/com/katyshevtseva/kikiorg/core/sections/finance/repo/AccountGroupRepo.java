package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountGroupRepo extends JpaRepository<AccountGroup, Long> {
}
