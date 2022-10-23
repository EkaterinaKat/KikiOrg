package com.katyshevtseva.kikiorg.core.sections.finance.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemGroupRepo extends JpaRepository<ItemGroup, Long> {
    List<ItemGroup> findByParentGroupIsNull();

    List<ItemGroup> findByParentGroup(ItemGroup itemGroup);
}
