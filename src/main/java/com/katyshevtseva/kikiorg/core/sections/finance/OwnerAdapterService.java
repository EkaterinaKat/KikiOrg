package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.*;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    List<ItemGroup> getGroupsByParentForCurrentUser(ItemGroup parent) {
        return itemGroupRepo.findByParentGroupAndOwner(parent, ownerService.getCurrentOwner());
    }

    List<Item> getItemsByParentForCurrentUser(ItemGroup parent) {
        return itemRepo.findByParentGroupAndOwner(parent, ownerService.getCurrentOwner());
    }

    List<ItemGroup> getTopLevelGroupsForCurrentUser() {
        return itemGroupRepo.findByParentGroupIsNullAndOwner(ownerService.getCurrentOwner());
    }

    List<Item> getTopLevelItemsForCurrentUser() {
        return itemRepo.findByParentGroupIsNullAndOwner(ownerService.getCurrentOwner());
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
