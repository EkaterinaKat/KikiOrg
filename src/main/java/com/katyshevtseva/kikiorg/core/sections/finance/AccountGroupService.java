package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.AccountGroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountGroupService {
    private final AccountGroupRepo repo;

    public void create(String title, List<Account> accounts) throws Exception {
        validateAccountGroup(accounts);
        repo.save(new AccountGroup(title, accounts));
    }

    public static void validateAccountGroup(List<Account> accounts) throws Exception {
        if (accounts == null || accounts.isEmpty()) {
            throw new Exception("Пустой список счетов");
        }
        if (accounts.stream().map(Account::getCurrency).distinct().count() > 1) {
            throw new Exception("Группа должна содержать счета одной валюты");
        }
    }

    public List<AccountGroup> getAll() {
        return repo.findAll();
    }

    public void delete(AccountGroup group) {
        repo.delete(group);
    }
}
