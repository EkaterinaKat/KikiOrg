package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.*;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class OwnerAdapterService {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ItemGroupRepo itemGroupRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private SourceRepo sourceRepo;
    @Autowired
    private
    ItemHierarchyLeafRepo itemHierarchyLeafRepo;

    Optional<ItemHierarchyLeaf> getLeafByItemForCurrentUser(Item item) {
        return itemHierarchyLeafRepo.findByOwnerAndItem(ownerService.getCurrentOwner(), item);
    }

    List<ItemHierarchyLeaf> getLeavesByParentForCurrentUser(ItemGroup parent) {
        return itemHierarchyLeafRepo.findByOwnerAndParentGroup(ownerService.getCurrentOwner(), parent);
    }

    List<ItemGroup> getGroupsByParentForCurrentUser(ItemGroup parent) {
        return itemGroupRepo.findByParentGroupAndOwner(parent, ownerService.getCurrentOwner());
    }

    List<ItemGroup> getTopLevelGroupsForCurrentUser() {
        return itemGroupRepo.findByParentGroupIsNullAndOwner(ownerService.getCurrentOwner());
    }

    List<Source> getSourcesForCurrentUser() {
        return sourceRepo.findAllByOwner(ownerService.getCurrentOwner());
    }

    List<Item> getItemsForCurrentOwner() {
        return itemRepo.findAllByOwner(ownerService.getCurrentOwner());
    }

    List<Account> getAccountsForCurrentUser() {
        return accountRepo.findAllByOwner(ownerService.getCurrentOwner());
    }

    void saveItemHierarchyLeaf(ItemHierarchyLeaf leaf) {
        leaf.setOwner(ownerService.getCurrentOwner());
        itemHierarchyLeafRepo.save(leaf);
    }

    void saveItemGroup(ItemGroup itemGroup) {
        itemGroup.setOwner(ownerService.getCurrentOwner());
        itemGroupRepo.save(itemGroup);
    }

    void saveItem(Item item) {
        item.setOwner(ownerService.getCurrentOwner());
        itemRepo.save(item);
    }

    void saveSource(Source source) {
        source.setOwner(ownerService.getCurrentOwner());
        sourceRepo.save(source);
    }

    void saveAccount(Account account) {
        account.setOwner(ownerService.getCurrentOwner());
        accountRepo.save(account);
    }

}
