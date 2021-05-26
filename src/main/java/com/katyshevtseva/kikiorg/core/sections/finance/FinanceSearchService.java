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
                operations.addAll(expenseRepo.search(request.getMinAmount(), request.getMaxAmount()));
                break;
            case TRANSFER:
                operations.addAll(transferRepo.search(request.getMinAmount(), request.getMaxAmount()));
                break;
            case REPLENISHMENT:
                operations.addAll(replenishmentRepo.search(request.getMinAmount(), request.getMaxAmount()));
        }
        System.out.println(operations.size());
        return operations;
    }

    @Data
    public static class SearchRequest {
        private Period period;
        private long minAmount;
        private long maxAmount;
        private OperationType operationType;
        private List<OperationEnd> from;
        private List<OperationEnd> to;
    }

    public interface OperationEnd {

    }
}
