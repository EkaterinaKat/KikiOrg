package com.katyshevtseva.kikiorg.core.modes.finance;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.*;
import com.katyshevtseva.kikiorg.core.repo.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FinanceService implements InitializingBean {
    private static FinanceService INSTANCE;
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
    private AccountPartRepo accountPartRepo;
    @Autowired
    private DateService dateService;
    @Autowired
    private TransferRepo transferRepo;

    public static FinanceService getInstance() {
        while (INSTANCE == null) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }

    public void addSourse(String title, String desc) {
        Source source = new Source();
        source.setTitle(title);
        source.setDescription(desc);
        source.setOwner(currentOwner);
        sourceRepo.save(source);
    }

    public List<Source> getSources() {
        return sourceRepo.findAll();
    }

    public void addItem(String title, String desc) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        item.setOwner(currentOwner);
        itemRepo.save(item);
    }

    public List<Item> getItems() {
        return itemRepo.findAll();
    }

    public void addAccount(String title, String desc, Owner owner) {
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
        account.setOwner(owner);
        accountRepo.save(account);
    }

    public List<Account> getAccounts() {
        return accountRepo.findAll();
    }

    public void addExpence(Account account, long amount, Item item, Date date) {
        Expense expense = new Expense();
        expense.setAccount(account);
        expense.setAmount(amount);
        expense.setDateOfExp(date);
        expense.setItem(item);
        expense.setOwner(currentOwner);
        expenseRepo.save(expense);

        addToAccountAmount(account, (-1) * amount);
    }

    public List<Expense> getExpenses() {
        return expenseRepo.findAll();
    }

    public void addReplenishment(Account account, long amount, Source source, Date date) {
        Replenishment replenishment = new Replenishment();
        replenishment.setAccount(account);
        replenishment.setAmount(amount);
        replenishment.setSource(source);
        replenishment.setDateOfRepl(date);
        replenishment.setOwner(currentOwner);
        replenishmentRepo.save(replenishment);

        addToAccountAmount(account, amount);
    }

    private void addToAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(actualAccount.getAmount() + amount);
        accountRepo.save(actualAccount);
    }

    List<Replenishment> getReplenishments() {
        return replenishmentRepo.findAll();
    }

    public List<AccountPart> getAccountParts() {
        return accountPartRepo.findAll();
    }

    public void rewriteAccountParts(List<AccountPart> accountParts) {
        accountPartRepo.deleteAll();
        accountPartRepo.saveAll(accountParts);
    }

    public String check(Account account, int amount) {
        long accountAmount = accountRepo.findById(account.getId()).get().getAmount();
        long diff = amount - accountAmount;
        return String.format("По расчетам: %s. По Факту: %s. Разница: %s.", accountAmount, amount, diff);
    }

    @Transactional
    public void makeTransfer(Account from, Account to, Long amount) {
        Transfer transfer = new Transfer();
        transfer.setFrom(from);
        transfer.setTo(to);
        transfer.setAmount(amount);
        transfer.setDateEntity(dateService.createIfNotExistAndGetDateEntity(new Date()));
        transfer.setOwner(currentOwner);
        transferRepo.save(transfer);

        addToAccountAmount(from, (-1) * amount);
        addToAccountAmount(to, amount);
    }

    public void validateAllAccountsAmount(){
        for(Account account: accountRepo.findAll()){
            validateAccountAmount(account);
        }
    }

    private void validateAccountAmount(Account account){
        List<Expense> expenses = expenseRepo.findByAccountId(account.getId());
        List<Replenishment> replenishments = replenishmentRepo.findByAccountId(account.getId());
        long amount = 0;
        for (Replenishment replenishment: replenishments){
            amount+=replenishment.getAmount();
        }
        for (Expense expense: expenses){
            amount-=expense.getAmount();
        }
        rewriteAccountAmount(account, amount);
    }

    private void rewriteAccountAmount(Account account, long amount){
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(amount);
        accountRepo.save(actualAccount);
    }
}
