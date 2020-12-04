package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorg.core.date.DateUtils.getMonthAgoDate;

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
    private AccountValidationService validationService;

    public List<Operation> getOperationsAvailableForCurrentUser() {
        List<Operation> operations = new ArrayList<>();
        operations.addAll(financeService.getReplenishmentsForCuByPeriod(getMonthAgoDate(), new Date()));
        operations.addAll(financeService.getExpensesForCuByPeriod(getMonthAgoDate(), new Date()));
        operations.addAll(financeService.getTransfersForCuByPeriod(getMonthAgoDate(), new Date()));
        operations.sort(Comparator.comparing(Operation::getDate).reversed());
        return operations;
    }

    public void deleteOperation(Operation operation) {
        switch (operation.getType()) {
            case EXPENSE:
                expenseRepo.deleteById(operation.getId());
                break;
            case TRANSFER:
                transferRepo.deleteById(operation.getId());
                break;
            case REPLENISHMENT:
                replenishmentRepo.deleteById(operation.getId());
        }
        validationService.validateAllAccountsAndRewriteAmounts();
    }

    public enum OperationType {
        TRANSFER, EXPENSE, REPLENISHMENT
    }

    public interface Operation {
        Date getDate();

        public Long getId();

        public String getFromTitle();

        public String getToTitle();

        public String getDateString();

        public String getAmount();

        public OperationType getType();
    }

}
