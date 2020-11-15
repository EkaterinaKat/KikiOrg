package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.*;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FinanceService {
    @Getter
    @Setter
    private Owner currentOwner = Owner.K;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private SourceRepo sourceRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private ReplenishmentRepo replenishmentRepo;
    @Autowired
    private DateService dateService;
    @Autowired
    private TransferRepo transferRepo;

    public void addSourse(String title, String desc) {
        Source source = new Source();
        source.setTitle(title);
        source.setDescription(desc);
        source.setOwner(currentOwner);
        sourceRepo.save(source);
    }

    public List<Source> getSources() {
        return sourceRepo.findAllByOwner(currentOwner);
    }

    public void addItem(String title, String desc) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        item.setOwner(currentOwner);
        itemRepo.save(item);
    }

    public List<Item> getItems() {
        return itemRepo.findAllByOwner(currentOwner);
    }

    public void addAccount(String title, String desc, Owner owner) {
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
        account.setOwner(owner);
        accountRepo.save(account);
    }

    public List<Account> getAccountsAvailableForCurrentOwner() {
        List<Owner> owners = getAvailableAccountOwners();
        List<Account> accounts = new ArrayList<>();
        for (Owner owner : owners) {
            accounts.addAll(accountRepo.findAllByOwner(owner));
        }
        return accounts;
    }

    public void addExpense(Account account, long amount, Item item, Date date) {
        Expense expense = new Expense();
        expense.setAccount(account);
        expense.setAmount(amount);
        expense.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
        expense.setItem(item);
        expenseRepo.save(expense);

        addToAccountAmount(account, (-1) * amount);
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        for (Account account : getAccountsAvailableForCurrentOwner())
            expenses.addAll(expenseRepo.findByAccount(account));
        expenses.sort(Comparator.comparing(Expense::getDateEntity));
        return expenses;
    }

    public void addReplenishment(Account account, long amount, Source source, Date date) {
        Replenishment replenishment = new Replenishment();
        replenishment.setAccount(account);
        replenishment.setAmount(amount);
        replenishment.setSource(source);
        replenishment.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
        replenishmentRepo.save(replenishment);

        addToAccountAmount(account, amount);
    }

    private void addToAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(actualAccount.getAmount() + amount);
        accountRepo.save(actualAccount);
    }

    // Если нет мд public то FinanceService не может получить доступ к этому методу в собраном в exe приложении
    public List<Replenishment> getReplenishments() {
        List<Replenishment> replenishments = new ArrayList<>();
        for (Account account : getAccountsAvailableForCurrentOwner())
            replenishments.addAll(replenishmentRepo.findByAccount(account));
        replenishments.sort(Comparator.comparing(Replenishment::getDateEntity));
        return replenishments;
    }

    @Transactional
    public void makeTransfer(Account from, Account to, Long amount) {
        Transfer transfer = new Transfer();
        transfer.setFrom(from);
        transfer.setTo(to);
        transfer.setAmount(amount);
        transfer.setDateEntity(dateService.createIfNotExistAndGetDateEntity(new Date()));
        transferRepo.save(transfer);

        addToAccountAmount(from, (-1) * amount);
        addToAccountAmount(to, amount);
    }

    public List<Transfer> getTransfers() {
        Set<Transfer> transferSet = new HashSet<>();
        for (Account account : getAccountsAvailableForCurrentOwner()) {
            transferSet.addAll(transferRepo.findAllByFrom(account));
            transferSet.addAll(transferRepo.findAllByTo(account));
        }
        List<Transfer> transferList = new ArrayList<>(transferSet);
        transferList.sort(Comparator.comparing(Transfer::getDateEntity));
        return transferList;
    }

    public void validateAllAccountsAmount() {
        for (Account account : accountRepo.findAll()) {
            validateAccountAmount(account);
        }
    }

    @Transactional
    private void validateAccountAmount(Account account) {
        List<Expense> expenses = expenseRepo.findByAccount(account);
        List<Replenishment> replenishments = replenishmentRepo.findByAccount(account);
        List<Transfer> transfersToAccount = transferRepo.findAllByTo(account);
        List<Transfer> transfersFromAccount = transferRepo.findAllByFrom(account);
        long amount = 0;
        for (Replenishment replenishment : replenishments) {
            amount += replenishment.getAmount();
        }
        for (Expense expense : expenses) {
            amount -= expense.getAmount();
        }
        for (Transfer transfer : transfersToAccount) {
            amount += transfer.getAmount();
        }
        for (Transfer transfer : transfersFromAccount) {
            amount -= transfer.getAmount();
        }
        rewriteAccountAmount(account, amount);
    }

    private void rewriteAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(amount);
        accountRepo.save(actualAccount);
    }

    public List<Owner> getAvailableAccountOwners() {
        if (currentOwner == Owner.K)
            return Arrays.asList(Owner.K, Owner.C);
        if (currentOwner == Owner.M)
            return Arrays.asList(Owner.M, Owner.C);
        throw new RuntimeException();
    }
}
