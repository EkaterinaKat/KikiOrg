package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {

    List<Item> findByParentGroup(ItemGroup itemGroup);
}
