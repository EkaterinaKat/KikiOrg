package com.katyshevtseva.kikiorg.database;

import com.katyshevtseva.kikiorg.core.finance.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
}
