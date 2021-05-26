package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceSearchService {
    private final ExpenseRepo expenseRepo;
    private final ReplenishmentRepo replenishmentRepo;
    private final TransferRepo transferRepo;

    public List<Operation> search(SearchRequest request) {
        List<Operation> operations = new ArrayList<>();

        switch (request.getOperationType()) {
            case EXPENSE:
                operations.addAll(expenseRepo.findAll());
                break;
            case TRANSFER:
                operations.addAll(transferRepo.findAll());
                break;
            case REPLENISHMENT:
                operations.addAll(replenishmentRepo.findAll());
        }
        return operations;
    }

    @Data
    public static class SearchRequest {
        private Period period;
        private int minAmount;
        private int maxAmount;
        private OperationType operationType;
        private List<OperationEnd> from;
        private List<OperationEnd> to;
    }

    public interface OperationEnd {

    }
}
