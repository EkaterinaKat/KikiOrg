package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountValidationService {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private TransferRepo transferRepo;
    @Autowired
    private ReplenishmentRepo replenishmentRepo;

    @Transactional
    void validateAllAccountsAndRewriteAmounts() {
        for (Account account : accountRepo.findAll()) {
            rewriteAccountAmount(account, calculateAccountAmountByOperations(account));
        }
    }

    public String validateAllAccountsAndGetReport() {
        String report = "";
        for (Account account : accountRepo.findAll()) {
            long calculatedAmount = calculateAccountAmountByOperations(account);
            report += String.format("%s: %s \n", account.getTitle(),
                    calculatedAmount == account.getAmount() ? "SUCCESS" : String.format(
                            "Error: actual amount - %d, calculated amount - %d", account.getAmount(), calculatedAmount));
        }
        return report;
    }

    private long calculateAccountAmountByOperations(Account account) {
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
        return amount;
    }

    private void rewriteAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(amount);
        accountRepo.save(actualAccount);
    }
}
