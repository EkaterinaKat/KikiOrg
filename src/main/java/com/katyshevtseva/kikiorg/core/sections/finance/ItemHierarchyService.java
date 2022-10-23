package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.hierarchy.HierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ItemGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ItemRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemHierarchyService extends HierarchyService<Item, ItemGroup> {
    private final ItemGroupRepo itemGroupRepo;
    private final ItemRepo itemRepo;

    @Override
    protected void createNewGroup(String s) {
        ItemGroup group = new ItemGroup();
        group.setTitle(s);
        itemGroupRepo.save(group);
    }

    @Override
    protected void saveModifiedGroup(ItemGroup itemGroup) {
        itemGroupRepo.save(itemGroup);
    }

    @Override
    protected void saveModifiedLeaf(Item item) {
        itemRepo.save(item);
    }

    @Override
    public List<ItemGroup> getAllGroups() {
        return itemGroupRepo.findAll();
    }

    @Override
    protected List<Item> getAllLeaves() {
        return itemRepo.findAll();
    }

    @Override
    protected void deleteGroup(ItemGroup itemGroup) {
        itemGroupRepo.delete(itemGroup);
    }

    @Override
    protected List<Item> getLeavesByParentGroup(ItemGroup itemGroup) {
        return itemRepo.findByParentGroup(itemGroup);
    }

    @Override
    protected List<ItemGroup> getGroupsByParentGroup(ItemGroup itemGroup) {
        return itemGroupRepo.findByParentGroup(itemGroup);
    }
}
