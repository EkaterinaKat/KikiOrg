package com.katyshevtseva.kikiorg.core.sections.finance;

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
import java.util.List;

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
        for (Replenishment replenishment : financeService.getReplenishmentsAvailableForCurrentUser()) {
            operations.add(convertToOperation(replenishment));
        }
        for (Expense expense : financeService.getExpensesAvailableForCurrentUser()) {
            operations.add(convertToOperation(expense));
        }
        for (Transfer transfer : financeService.getTransfersAvailableForCurrentUser()) {
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
                Operation.OperationType.REPLENISHMENT);
    }

    private Operation convertToOperation(Expense expense) {
        return new Operation(
                expense.getId(),
                expense.getAccount().getTitle(),
                expense.getItem().getTitle(),
                expense.getDateEntity().getValue(),
                expense.getAmount(),
                Operation.OperationType.EXPENSE
        );
    }

    private Operation convertToOperation(Transfer transfer) {
        return new Operation(
                transfer.getId(),
                transfer.getFrom().getTitle(),
                transfer.getTo().getTitle(),
                transfer.getDateEntity().getValue(),
                transfer.getAmount(),
                Operation.OperationType.TRANSFER
        );
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
}
