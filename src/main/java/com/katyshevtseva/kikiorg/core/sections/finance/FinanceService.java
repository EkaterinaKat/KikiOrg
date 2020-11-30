package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.*;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FinanceService {
    @Autowired
    private OwnerAdapterService adapter;
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

    public void addSource(String title, String desc) {
        Source source = new Source();
        source.setTitle(title);
        source.setDescription(desc);
        adapter.saveSource(source);
    }

    public List<Source> getSourcesForCurrentUser() {
        return adapter.getSourcesForCurrentUser();
    }

    public void addItem(String title, String desc) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        adapter.saveItem(item);
    }

    public List<Item> getItemsForCurrentOwner() {
        return adapter.getItemsForCurrentOwner();
    }

    public void addAccount(String title, String desc) {
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
        adapter.saveAccount(account);
    }

    public List<Account> getAccountsForCurrentUser() {
        return adapter.getAccountsForCurrentUser();
    }

    public List<Account> getAccountsForTransferSection() {
        return adapter.getAccountsForTransferSection();
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

    // Если нет мд public то FinanceService не может получить доступ к этому методу в собраном в exe приложении
    public List<Expense> getExpensesForCurrentUser() {
        List<Expense> expenses = new ArrayList<>();
        for (Account account : getAccountsForCurrentUser())
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

    // Если нет мд public то FinanceService не может получить доступ к этому методу в собраном в exe приложении
    public List<Replenishment> getReplenishmentsForCurrentUser() {
        List<Replenishment> replenishments = new ArrayList<>();
        for (Account account : getAccountsForCurrentUser())
            replenishments.addAll(replenishmentRepo.findByAccount(account));
        replenishments.sort(Comparator.comparing(Replenishment::getDateEntity));
        return replenishments;
    }

    public void alterAccount(Account account, String newTitle, String newDesc) {
        account.setTitle(newTitle);
        account.setDescription(newDesc);
        accountRepo.save(account);
    }

    public void alterItem(Item item, String newTitle, String newDesc) {
        item.setTitle(newTitle);
        item.setDescription(newDesc);
        itemRepo.save(item);
    }

    public void alterSource(Source source, String newTitle, String newDesc) {
        source.setTitle(newTitle);
        source.setDescription(newDesc);
        sourceRepo.save(source);
    }

    @Transactional
    public void addTransfer(Account from, Account to, Long amount) {
        Transfer transfer = new Transfer();
        transfer.setFrom(from);
        transfer.setTo(to);
        transfer.setAmount(amount);
        transfer.setDateEntity(dateService.createIfNotExistAndGetDateEntity(new Date()));
        transferRepo.save(transfer);

        addToAccountAmount(from, (-1) * amount);
        addToAccountAmount(to, amount);
    }

    // Если нет мд public то FinanceService не может получить доступ к этому методу в собраном в exe приложении
    public List<Transfer> getTransfersForCurrentUser() {
        Set<Transfer> transferSet = new HashSet<>();
        for (Account account : getAccountsForCurrentUser()) {
            transferSet.addAll(transferRepo.findAllByFrom(account));
            transferSet.addAll(transferRepo.findAllByTo(account));
        }
        List<Transfer> transferList = new ArrayList<>(transferSet);
        transferList.sort(Comparator.comparing(Transfer::getDateEntity));
        return transferList;
    }

    private void addToAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(actualAccount.getAmount() + amount);
        accountRepo.save(actualAccount);
    }

    public void validateAllAccountsAmount() {
        for (Account account : accountRepo.findAll()) {
            validateAccountAmount(account);
        }
    }

    @Transactional
    void validateAccountAmount(Account account) {
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
}
