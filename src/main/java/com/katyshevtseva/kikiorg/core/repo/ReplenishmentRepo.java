package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Replenishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplenishmentRepo extends JpaRepository<Replenishment, Long> {
    List<Replenishment> findByAccountId(Long accountId);
    List<Replenishment> findAllByOwner(Owner owner);
}
