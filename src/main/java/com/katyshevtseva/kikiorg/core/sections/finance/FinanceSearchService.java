package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.repo.ExpenseRepo;
import com.katyshevtseva.kikiorg.core.repo.ReplenishmentRepo;
import com.katyshevtseva.kikiorg.core.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
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

        return operations;
    }

    @Data
    private static class SearchRequest {
        Period period;
        int minAmount;
        int maxAmount;
    }

    @Data
    public static class ExpenseSearchRequest extends SearchRequest {
        private List<Item> items;
        private List<Account> accounts;

    }

    @Data
    public static class ReplenishmentSearchRequest extends SearchRequest {
        private List<Account> from;
        private List<Account> to;
    }

    @Data
    public static class TransferSearchRequest extends SearchRequest {
        private List<Source> sources;
        private List<Account> accounts;
    }
}