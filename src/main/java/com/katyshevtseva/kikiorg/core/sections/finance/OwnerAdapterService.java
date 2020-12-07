package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.*;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OwnerAdapterService {
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

    List<ItemGroup> getItemGroupsForCurrentUser() {
        return itemGroupRepo.findByOwner(ownerService.getCurrentOwner());
    }

    List<Source> getSourcesForCurrentUser() {
        List<Source> sources = new ArrayList<>();
        for (Owner owner : getScope())
            sources.addAll(sourceRepo.findAllByOwner(owner));
        return sources;
    }

    List<Item> getItemsForCurrentOwner() {
        List<Item> items = new ArrayList<>();
        for (Owner owner : getScope())
            items.addAll(itemRepo.findAllByOwner(owner));
        return items;
    }

    public List<Account> getAccountsForCurrentUser() {
        return accountRepo.findAllByOwner(ownerService.getCurrentOwner());
    }

    List<Account> getAccountsForTransferSection() {
        List<Account> accounts = new ArrayList<>();
        for (Owner owner : getScope())
            accounts.addAll(accountRepo.findAllByOwner(owner));
        return accounts;
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

    private List<Owner> getScope() {
        List<Owner> scope = new ArrayList<>();
        scope.add(Owner.C);

        switch (ownerService.getCurrentOwner()) {
            case K:
                scope.add(Owner.K);
                break;
            case M:
                scope.add(Owner.M);
        }
        return scope;
    }

}
