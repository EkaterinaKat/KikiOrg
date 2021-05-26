package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    List<Expense> findByAccount(Account account);

    List<Expense> findByAccountAndDateEntity(Account account, DateEntity dateEntity);

    List<Expense> findByItemAndDateEntityAndAccount(Item item, DateEntity dateEntity, Account account);

    @Query("SELECT e FROM Expense e WHERE " +
            "e.amount BETWEEN :minAmount AND :maxAmount " +
            "AND e.dateEntity.value BETWEEN :startDate AND :endDate ")
    List<Expense> search(
            @Param("minAmount") long minAmount,
            @Param("maxAmount") long maxAmount,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT e FROM Expense e WHERE " +
            "e.amount > :minAmount AND e.amount < :maxAmount " +
            "AND e.dateEntity.value BETWEEN :startDate AND :endDate " +
            "AND e.account IN :accounts " +
            "AND e.item IN :items ")
    List<Expense> search1(  //todo удалить потом
                            @Param("minAmount") int minAmount,
                            @Param("maxAmount") int maxAmount,
                            @Param("startDate") Date startDate,
                            @Param("endDate") Date endDate,
                            @Param("accounts") List<Account> accounts,
                            @Param("items") List<Item> items);
}
