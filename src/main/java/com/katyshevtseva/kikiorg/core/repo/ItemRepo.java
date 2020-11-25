package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner(OwnerService.Owner owner);

    List<Item> findByParentGroupIsNull();

    List<Item> findByParentGroup(ItemGroup itemGroup);
}
