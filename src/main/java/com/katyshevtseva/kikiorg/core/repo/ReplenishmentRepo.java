package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReplenishmentRepo extends JpaRepository<Replenishment, Long> {
    List<Replenishment> findByAccount(Account account);

    List<Replenishment> findByAccountAndDateEntity(Account account, DateEntity dateEntity);

    @Query("SELECT r FROM Replenishment r WHERE " +
            "r.amount BETWEEN :minAmount AND :maxAmount " +
            "AND r.dateEntity.value BETWEEN :startDate AND :endDate " +
            "AND r.source IN :from " +
            "AND r.account IN :to ")
    List<Replenishment> search(
            @Param("minAmount") long minAmount,
            @Param("maxAmount") long maxAmount,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("from") List<Source> from,
            @Param("to") List<Account> to);
}
