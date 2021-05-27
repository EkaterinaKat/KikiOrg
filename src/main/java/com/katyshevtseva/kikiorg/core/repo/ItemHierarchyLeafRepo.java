package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemHierarchyLeaf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemHierarchyLeafRepo extends JpaRepository<ItemHierarchyLeaf, Long> {
    Optional<ItemHierarchyLeaf> findByItem(Item item);

    List<ItemHierarchyLeaf> findByParentGroup(ItemGroup parentGroup);
}
