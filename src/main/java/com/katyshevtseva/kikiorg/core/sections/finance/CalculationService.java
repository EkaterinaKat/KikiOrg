package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationService {
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private TransferRepo transferRepo;
    @Autowired
    private ReplenishmentRepo replenishmentRepo;

    public long calculateAccountAmountByOperations(Account account) {
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
}
