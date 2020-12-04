package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepo extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByFrom(Account from);

    List<Transfer> findAllByTo(Account to);

    List<Transfer> findAllByFromAndDateEntity(Account from, DateEntity dateEntity);

    List<Transfer> findAllByToAndDateEntity(Account to, DateEntity dateEntity);
}
