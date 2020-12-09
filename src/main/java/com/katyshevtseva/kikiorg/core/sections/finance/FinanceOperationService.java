package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
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
    private AccountAmountCalculationService calculationService;

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
                calculationService.recalculateAndRewriteAccountAmount(((Expense) operation).getAccount());
                break;
            case REPLENISHMENT:
                replenishmentRepo.deleteById(operation.getId());
                calculationService.recalculateAndRewriteAccountAmount(((Replenishment) operation).getAccount());
                break;
            case TRANSFER:
                transferRepo.deleteById(operation.getId());
                calculationService.recalculateAndRewriteAccountAmount(((Transfer) operation).getTo());
                calculationService.recalculateAndRewriteAccountAmount(((Transfer) operation).getFrom());
        }
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
