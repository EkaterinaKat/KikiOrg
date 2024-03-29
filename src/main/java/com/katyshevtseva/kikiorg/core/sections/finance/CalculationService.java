package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculationService {
    private final ExpenseRepo expenseRepo;
    private final TransferRepo transferRepo;
    private final ReplenishmentRepo replenishmentRepo;

    public long calculateAccountAmountByOperations(Account account) {
        List<Expense> expenses = expenseRepo.findByAccount(account);
        List<Replenishment> replenishments = replenishmentRepo.findByAccount(account);
        List<Transfer> transfersToAccount = transferRepo.findAllByTo(account);
        List<Transfer> transfersFromAccount = transferRepo.findAllByFrom(account);
        long amount = 0;

        long replenishmentAmount = replenishments.stream().map(Replenishment::getAmount).reduce(Long::sum).orElse(0L);
        amount += replenishmentAmount;

        long expenseAmount = expenses.stream().map(Expense::getAmount).reduce(Long::sum).orElse(0L);
        amount -= expenseAmount;

        long transfersToAccountAmount = transfersToAccount.stream().map(Transfer::getCameAmount).reduce(Long::sum).orElse(0L);
        amount += transfersToAccountAmount;

        long transfersFromAccountAmount = transfersFromAccount.stream().map(Transfer::getGoneAmount).reduce(Long::sum).orElse(0L);
        amount -= transfersFromAccountAmount;

        return amount;
    }
}
