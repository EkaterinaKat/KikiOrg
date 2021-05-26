package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            case EXPENSE:
                operations.addAll(expenseRepo.search(
                        request.getMinAmount(),
                        request.getMaxAmount(),
                        request.getPeriod().start(),
                        request.getPeriod().end(),
                        request.getFrom().stream().map(operationEnd -> (Account) operationEnd).collect(Collectors.toList()),
                        request.getTo().stream().map(operationEnd -> (Item) operationEnd).collect(Collectors.toList())));
                break;
            case TRANSFER:
                operations.addAll(transferRepo.search(
                        request.getMinAmount(),
                        request.getMaxAmount(),
                        request.getPeriod().start(),
                        request.getPeriod().end(),
                        request.getFrom().stream().map(operationEnd -> (Account) operationEnd).collect(Collectors.toList()),
                        request.getTo().stream().map(operationEnd -> (Account) operationEnd).collect(Collectors.toList())));
                break;
            case REPLENISHMENT:
                operations.addAll(replenishmentRepo.search(
                        request.getMinAmount(),
                        request.getMaxAmount(),
                        request.getPeriod().start(),
                        request.getPeriod().end(),
                        request.getFrom().stream().map(operationEnd -> (Source) operationEnd).collect(Collectors.toList()),
                        request.getTo().stream().map(operationEnd -> (Account) operationEnd).collect(Collectors.toList())));
        }
        System.out.println(operations.size());
        return operations;
    }

    @Data
    public static class SearchRequest { //todo логику обработки нулевых запросов поместить сюда
        private Period period;
        private long minAmount;
        private long maxAmount;
        private OperationType operationType;
        private List<OperationEnd> from;
        private List<OperationEnd> to;
    }

    public interface OperationEnd {

    }

    public List<OperationEnd> getFrom(OperationType operationType) {
        List<OperationEnd> operationEndList = new ArrayList<>();
        switch (operationType) {
            case REPLENISHMENT:
                operationEndList.addAll(financeService.getSourcesForCurrentUser());
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
                operationEndList.addAll(financeService.getItemsForCurrentOwner());
                break;
            case REPLENISHMENT:
            case TRANSFER:
                operationEndList.addAll(financeService.getAllAccounts());
        }
        return operationEndList;
    }
}
