package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplenishmentRepo extends JpaRepository<Replenishment, Long>, JpaSpecificationExecutor<Replenishment> {
    List<Replenishment> findByAccount(Account account);

    List<Replenishment> findByAccountAndDateEntity(Account account, DateEntity dateEntity);
}
