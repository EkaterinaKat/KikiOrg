package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.Period;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType.ALL;

@Service
public class FinanceOperationService {
    @Autowired
    private FinanceService financeService;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private ReplenishmentRepo replenishmentRepo;
    @Autowired
    private TransferRepo transferRepo;
    @Autowired
    private CalculationService calculationService;
    @Autowired
    private AccountRepo accountRepo;

    public List<Operation> getOperationsAvailableForCurrentUser() {
        List<Operation> operations = new ArrayList<>();
        operations.addAll(financeService.getReplenishmentsForCuByPeriod(Period.getLastMonth()));
        operations.addAll(financeService.getExpensesForCuByPeriod(Period.getLastMonth()));
        operations.addAll(financeService.getTransfersForCuByPeriod(Period.getLastMonth(), ALL));
        operations.sort(Comparator.comparing(Operation::getDate).reversed());
        return operations;
    }

    public void deleteOperation(Operation operation) {
        switch (operation.getType()) {
            case EXPENSE:
                expenseRepo.deleteById(operation.getId());
                recalculateAndRewriteAccountAmount(((Expense) operation).getAccount());
                break;
            case REPLENISHMENT:
                replenishmentRepo.deleteById(operation.getId());
                recalculateAndRewriteAccountAmount(((Replenishment) operation).getAccount());
                break;
            case TRANSFER:
                transferRepo.deleteById(operation.getId());
                recalculateAndRewriteAccountAmount(((Transfer) operation).getTo());
                recalculateAndRewriteAccountAmount(((Transfer) operation).getFrom());
        }
    }

    private void recalculateAndRewriteAccountAmount(Account account) {
        rewriteAccountAmount(account, calculationService.calculateAccountAmountByOperations(account));
    }

    private void rewriteAccountAmount(Account account, long amount) {
        Account actualAccount = accountRepo.findById(account.getId()).orElse(null);
        actualAccount.setAmount(amount);
        accountRepo.save(actualAccount);
    }

    public enum OperationType {
        TRANSFER, EXPENSE, REPLENISHMENT
    }

    public interface Operation {
        Date getDate();

        public long getId();

        public String getFromTitle();

        public String getToTitle();

        public String getDateString();

        public String getAmountString();

        public OperationType getType();
    }

}
