package com.katyshevtseva.kikiorg.core.finance;

import com.katyshevtseva.kikiorg.core.finance.entity.*;
import com.katyshevtseva.kikiorg.database.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

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

    public static FinanceManager getInstance(){
        while (INSTANCE==null){
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

    public String getReport(){
        return reportGenerator.getReport();
    }

    public void addSourse(String title, String desc){
        Source source = new Source();
        source.setTitle(title);
        source.setDescription(desc);
        sourceRepo.save(source);
    }

    List<Source> getSources(){
        return sourceRepo.findAll();
    }

    public void addItem(String title, String desc){
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        itemRepo.save(item);
    }

    List<Item> getItems(){
        return itemRepo.findAll();
    }

    public void addAccount(String title, String desc){
        Account account = new Account();
        account.setTitle(title);
        account.setDescription(desc);
        account.setAmount(0);
        accountRepo.save(account);
    }

    List<Account> getAccounts(){
        return accountRepo.findAll();
    }

    public void addExpence (Account account, long amount, Item item, Date date){
        Expense expense = new Expense();
        expense.setAccount(account);
        expense.setAmount(amount);
        expense.setDateOfExp(date);
        expense.setItem(item);
        expenseRepo.save(expense);
    }

    List<Expense> getExpences(){
        return expenseRepo.findAll();
    }

    public void addReplenishment (Account account, long amount, Source source, Date date){
        Replenishment replenishment = new Replenishment();
        replenishment.setAccount(account);
        replenishment.setAmount(amount);
        replenishment.setSource(source);
        replenishment.setDateOfRepl(date);
        replenishmentRepo.save(replenishment);
    }

    List<Replenishment> getReplenishments(){
        return replenishmentRepo.findAll();
    }
}
