package com.katyshevtseva.kikiorg.database;

import com.katyshevtseva.kikiorg.core.finance.entity.Replenishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplenishmentRepo extends JpaRepository<Replenishment, Long> {
}
