package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(Owner owner);
}
