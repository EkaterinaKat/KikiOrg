package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.*;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.getLastMonthPeriod;
import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType.*;

@Service
public class FinanceService {
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
        sourceRepo.save(source);
    }

    public void addItem(String title, String desc) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        itemRepo.save(item);
    }

    // Возвращает список из 15 (или меньше) Item которые использовались самыми последними
    public List<Item> getFewLastItems() {
        List<Expense> expenses = getExpensesByPeriod(getLastMonthPeriod());
        expenses.sort(Comparator.comparing(Expense::getDateEntity).reversed());
        Set<Item> items = new HashSet<>();
        for (Expense expense : expenses) {
            items.add(expense.getItem());
            if (items.size() == 15)
                break;
        }
        return new ArrayList<>(items);
    }

    public void addAccount(String title, String desc) {
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
        accountRepo.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public List<Source> getAllSources() {
        return sourceRepo.findAll();
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll().stream().sorted(Comparator.comparing(Item::getTitle)).collect(Collectors.toList());
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
    public List<Expense> getExpensesByPeriod(Period period) {
        List<Expense> expenses = new ArrayList<>();
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(period);
        for (DateEntity dateEntity : dateEntities) {
            for (Account account : getAllAccounts())
                expenses.addAll(expenseRepo.findByAccountAndDateEntity(account, dateEntity));
        }
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
    public List<Replenishment> getReplenishmentsByPeriod(Period period) {
        List<Replenishment> replenishments = new ArrayList<>();
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(period);
        for (DateEntity dateEntity : dateEntities) {
            for (Account account : getAllAccounts())
                replenishments.addAll(replenishmentRepo.findByAccountAndDateEntity(account, dateEntity));
        }
        replenishments.sort(Comparator.comparing(Replenishment::getDateEntity));
        return replenishments;
    }

    @Transactional
    public void alterAccount(Account account, String newTitle, String newDesc) {
        account.setTitle(newTitle);
        account.setDescription(newDesc);
        accountRepo.save(account);
    }

    @Transactional
    public void alterItem(Item item, String newTitle, String newDesc) {
        item.setTitle(newTitle);
        item.setDescription(newDesc);
        itemRepo.save(item);
    }

    @Transactional
    public void alterSource(Source source, String newTitle, String newDesc) {
        source.setTitle(newTitle);
        source.setDescription(newDesc);
        sourceRepo.save(source);
    }

    @Transactional
    public void addTransfer(Account from, Account to, Long amount, Date date) {
        Transfer transfer = new Transfer();
        transfer.setFrom(from);
        transfer.setTo(to);
        transfer.setAmount(amount);
        transfer.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
        transferRepo.save(transfer);

        addToAccountAmount(from, (-1) * amount);
        addToAccountAmount(to, amount);
    }

    // Если нет мд public то FinanceService не может получить доступ к этому методу в собраном в exe приложении
    public List<Transfer> getTransfersForCuByPeriod(Period period, TransferType transferType) {
        Set<Transfer> transferSet = new HashSet<>();
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(period);
        for (DateEntity dateEntity : dateEntities) {
            for (Account account : getAllAccounts()) {
                if (transferType == TO_USER_ACCOUNTS || transferType == ALL)
                    transferSet.addAll(transferRepo.findAllByToAndDateEntity(account, dateEntity));
                if (transferType == FROM_USER_ACCOUNTS || transferType == ALL)
                    transferSet.addAll(transferRepo.findAllByFromAndDateEntity(account, dateEntity));
            }
        }
        List<Transfer> transferList = new ArrayList<>(transferSet);
        transferList.sort(Comparator.comparing(Transfer::getDateEntity));
        return transferList;
    }

    public enum TransferType {
        TO_USER_ACCOUNTS, FROM_USER_ACCOUNTS, ALL
    }

    private void addToAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(actualAccount.getAmount() + amount);
        accountRepo.save(actualAccount);
    }
}
