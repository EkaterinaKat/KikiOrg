package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.getLastMonthPeriod;

@Service
@RequiredArgsConstructor
public class FinanceService {
    private final AccountRepo accountRepo;
    private final SourceRepo sourceRepo;
    private final ItemRepo itemRepo;
    private final ExpenseRepo expenseRepo;
    private final ReplenishmentRepo replenishmentRepo;
    private final DateService dateService;
    private final TransferRepo transferRepo;
    private final OperationDeletionService operationDeletionService;

    public void addSource(String title, String desc) {
        Source source = new Source();
        source.setTitle(title);
        source.setDescription(desc);
        sourceRepo.saveAndFlush(source);
    }

    public void addItem(String title, String desc) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        itemRepo.saveAndFlush(item);
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

    public void addAccount(String title, String desc, Currency currency) {
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
        account.setArchived(false);
        account.setCurrency(currency);
        accountRepo.saveAndFlush(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll().stream().sorted(Comparator.comparing(Account::isArchived)).collect(Collectors.toList());
    }

    public List<Account> getActiveAccounts() {
        return accountRepo.findAllByArchivedFalse();
    }

    public List<Source> getAllSources() {
        return sourceRepo.findAll();
    }

    public List<Item> getAllItems() {
        return itemRepo.findAll().stream().sorted(Comparator.comparing(Item::getTitleWithAdditionalInfo)).collect(Collectors.toList());
    }

    public void addExpense(Account account, long amount, Item item, Date date, Necessity necessity, String comment) {
        Expense expense = new Expense();
        expense.setAccount(account);
        expense.setAmount(amount);
        expense.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
        expense.setItem(item);
        expense.setComment(comment);
        expense.setNecessity(necessity);
        expenseRepo.saveAndFlush(expense);

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
        replenishmentRepo.saveAndFlush(replenishment);

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
        accountRepo.saveAndFlush(account);
    }

    @Transactional
    public void alterItem(Item item, String newTitle, String newDesc) {
        item.setTitle(newTitle);
        item.setDescription(newDesc);
        itemRepo.saveAndFlush(item);
    }

    @Transactional
    public void alterSource(Source source, String newTitle, String newDesc) {
        source.setTitle(newTitle);
        source.setDescription(newDesc);
        sourceRepo.saveAndFlush(source);
    }

    // Если нет мд public то FinanceService не может получить доступ к этому методу в собраном в exe приложении
    public List<Transfer> getTransfersByPeriod(Period period) {
        Set<Transfer> transferSet = new HashSet<>();
        List<DateEntity> dateEntities = dateService.getOnlyExistingDateEntitiesByPeriod(period);
        for (DateEntity dateEntity : dateEntities) {
            for (Account account : getAllAccounts()) {
                transferSet.addAll(transferRepo.findAllByToAndDateEntity(account, dateEntity));
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

    public void addToAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(actualAccount.getAmount() + amount);
        accountRepo.saveAndFlush(actualAccount);
    }

    public void archive(Account account) {
        account.setArchived(!account.isArchived());
        accountRepo.save(account);
    }

    public void editExpense(Expense expense, Account account, long amount,
                            Item item, Date date, Necessity necessity, String comment) {
        operationDeletionService.deleteOperation(expense);
        addExpense(account, amount, item, date, necessity, comment);
    }
}
