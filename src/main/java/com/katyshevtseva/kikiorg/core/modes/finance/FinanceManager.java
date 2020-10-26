package com.katyshevtseva.kikiorg.core.modes.finance;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.*;
import com.katyshevtseva.kikiorg.core.repo.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class FinanceManager implements InitializingBean {
    private static FinanceManager INSTANCE;
    private ReportGenerator reportGenerator;
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

    public static FinanceManager getInstance() {
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
        reportGenerator = new ReportGenerator(INSTANCE);
    }

    public String getReport() {
        return reportGenerator.getReport();
    }

    public void addSourse(String title, String desc) {
        Source source = new Source();
        source.setTitle(title);
        source.setDescription(desc);
        sourceRepo.save(source);
    }

    public List<Source> getSources() {
        return sourceRepo.findAll();
    }

    public void addItem(String title, String desc) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        itemRepo.save(item);
    }

    public List<Item> getItems() {
        return itemRepo.findAll();
    }

    public void addAccount(String title, String desc) {
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
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
        expenseRepo.save(expense);

        changeAccountAmount(account, (-1) * amount);
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
        replenishmentRepo.save(replenishment);

        changeAccountAmount(account, amount);
    }

    private void changeAccountAmount(Account account, long amount) {
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
        transfer.setDateEntity(dateService.getDateEntity(new Date()));
        transferRepo.save(transfer);

        changeAccountAmount(from, (-1) * amount);
        changeAccountAmount(to, amount);
    }
}
