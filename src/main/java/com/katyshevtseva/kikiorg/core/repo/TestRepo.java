package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepo extends JpaRepository<Account, Long> {

    @Query(value = "select count(*) from replenishment r \n" +
            "join date_entity d on r.date_entity_id = d.id \n" +
            "join source s on r.source_id = s.id \n" +
            "join account a on r.account_id = a.id \n" +
            "where r.amount >= 600 \n" +
            "and d.value between '2020-11-05' and '2021-03-25' \n" +
            "and a.id in ?1 ", nativeQuery = true)
    long count1(List<Long> accountIds);

    @Query(value = "select count(*) from expense r \n" +
            "join date_entity d on r.date_entity_id = d.id \n" +
            "join item s on r.item_id = s.id \n" +
            "join account a on r.account_id = a.id \n" +
            "where r.amount between 600 and 1200 \n" +
            "and d.value <= '2021-01-25' \n" +
            "and a.id in ?1 ", nativeQuery = true)
    long count2(List<Long> accountIds);

    @Query(value = "select count(*) from transfer r \n" +
            "join date_entity d on r.date_entity_id = d.id \n" +
            "join account a1 on r.from_account_id = a1.id \n" +
            "join account a2 on r.to_account_id = a2.id \n" +
            "where r.amount <= 1500 \n" +
            "and d.value >= '2020-11-06' \n" +
            "and a2.id in ?1 ", nativeQuery = true)
    long count3(List<Long> accountIds);

    @Query(value = "select count(*) from replenishment r \n" +
            "join date_entity d on r.date_entity_id = d.id \n" +
            "join source s on r.source_id = s.id \n" +
            "join account a on r.account_id = a.id \n" +
            "where r.amount >= 10000 \n" +
            "and d.value <= '2021-02-15' \n" +
            "and s.id in ?1 ", nativeQuery = true)
    long count4(List<Long> sourceIds);

    @Query(value = "select count(*) from expense r \n" +
            "join date_entity d on r.date_entity_id = d.id \n" +
            "join item s on r.item_id = s.id \n" +
            "join account a on r.account_id = a.id \n" +
            "where r.amount between 1000 and 15000 \n" +
            "and d.value between '2020-10-11' and '2021-01-25' \n" +
            "and a.id in ?1 and s.id in ?2 ", nativeQuery = true)
    long count5(List<Long> accountIds, List<Long> itemIds);

    @Query(value = "select count(*) from transfer r ", nativeQuery = true)
    long count6();
}
