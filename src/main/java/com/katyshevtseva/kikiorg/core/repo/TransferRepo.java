package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransferRepo extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByFrom(Account from);

    List<Transfer> findAllByTo(Account to);

    List<Transfer> findAllByFromAndDateEntity(Account from, DateEntity dateEntity);

    List<Transfer> findAllByToAndDateEntity(Account to, DateEntity dateEntity);

    @Query("SELECT t FROM Transfer t WHERE " +
            "t.goneAmount BETWEEN :minAmount AND :maxAmount " +
            "AND t.dateEntity.value BETWEEN :startDate AND :endDate " +
            "AND t.from IN :from " +
            "AND t.to IN :to ")
    List<Transfer> search(
            @Param("minAmount") long minAmount,
            @Param("maxAmount") long maxAmount,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("from") List<Account> from,
            @Param("to") List<Account> to);
}
