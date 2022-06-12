package com.katyshevtseva.kikiorg.core.sections.finance.search;

import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceSearchService {
    private final FinanceService financeService;
    private final ExpenseRepo expenseRepo;
    private final ReplenishmentRepo replenishmentRepo;
    private final TransferRepo transferRepo;

    public List<Operation> search(SearchRequest request) {
        List<Operation> operations = new ArrayList<>();

        switch (request.getOperationType()) {
            case REPLENISHMENT:
                operations.addAll(replenishmentRepo.findAll(new ReplenishmentSpec(request)));
                break;
            case EXPENSE:
                operations.addAll(expenseRepo.findAll(new ExpenseSpec(request)));
                break;
            case TRANSFER:
                operations.addAll(transferRepo.findAll(new TransferSpec(request)));
        }
        return operations.stream().sorted(Comparator.comparing(Operation::getDate).reversed()).collect(Collectors.toList());
    }

    public List<OperationEnd> getFrom(OperationType operationType) {
        List<OperationEnd> operationEndList = new ArrayList<>();
        switch (operationType) {
            case REPLENISHMENT:
                operationEndList.addAll(financeService.getAllSources());
                break;
            case EXPENSE:
            case TRANSFER:
                operationEndList.addAll(financeService.getAllAccounts());
        }
        return operationEndList;
    }

    public List<OperationEnd> getTo(OperationType operationType) {
        List<OperationEnd> operationEndList = new ArrayList<>();
        switch (operationType) {
            case EXPENSE:
                operationEndList.addAll(financeService.getAllItems());
                break;
            case REPLENISHMENT:
            case TRANSFER:
                operationEndList.addAll(financeService.getAllAccounts());
        }
        return operationEndList;
    }
}
