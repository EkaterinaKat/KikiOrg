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

    public List<Operation> getOperationsAvailableForCurrentUser() {
        List<Operation> operations = new ArrayList<>();
        for (Replenishment replenishment : financeService.getReplenishmentsForCuByPeriod(getMonthAgoDate(), new Date())) {
            operations.add(convertToOperation(replenishment));
        }
        for (Expense expense : financeService.getExpensesForCuByPeriod(getMonthAgoDate(), new Date())) {
            operations.add(convertToOperation(expense));
        }
        for (Transfer transfer : financeService.getTransfersForCuByPeriod(getMonthAgoDate(), new Date())) {
            operations.add(convertToOperation(transfer));
        }
        operations.sort(Comparator.comparing(Operation::getDateForSorting).reversed());
        return operations;
    }

    private Operation convertToOperation(Replenishment replenishment) {
        return new Operation(
                replenishment.getId(),
                replenishment.getSource().getTitle(),
                replenishment.getAccount().getTitle(),
                replenishment.getDateEntity().getValue(),
                replenishment.getAmount(),
                OperationType.REPLENISHMENT);
    }

    private Operation convertToOperation(Expense expense) {
        return new Operation(
                expense.getId(),
                expense.getAccount().getTitle(),
                expense.getItem().getTitle(),
                expense.getDateEntity().getValue(),
                expense.getAmount(),
                OperationType.EXPENSE
        );
    }

    private Operation convertToOperation(Transfer transfer) {
        return new Operation(
                transfer.getId(),
                accountToString(transfer.getFrom()),
                accountToString(transfer.getTo()),
                transfer.getDateEntity().getValue(),
                transfer.getAmount(),
                OperationType.TRANSFER
        );
    }

    private String accountToString(Account account) {
        return String.format("%s (owner: %s)", account.getTitle(), account.getOwner());
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
        financeService.validateAllAccountsAmount();
    }

    public enum OperationType {
        TRANSFER, EXPENSE, REPLENISHMENT
    }

    public class Operation {
        private final Long id;
        private final String from;
        private final String to;
        private final Date date;
        private final Long amount;
        private final OperationType type;

        Operation(Long id, String from, String to, Date date, Long amount, OperationType type) {
            this.id = id;
            this.from = from;
            this.to = to;
            this.date = date;
            this.amount = amount;
            this.type = type;
        }

        Date getDateForSorting() {
            return date;
        }

        public Long getId() {
            return id;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getDate() {
            return date.toString();
        }

        public String getAmount() {
            return "" + amount;
        }

        public OperationType getType() {
            return type;
        }
    }

}
