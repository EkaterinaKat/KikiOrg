package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import com.katyshevtseva.kikiorg.core.sections.finance.search.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.search.SearchRequest;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.EXPENSE;
import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.TRANSFER;

@Service
@RequiredArgsConstructor
public class BaseReportService {
    private final FinanceSearchService searchService;
    private final FinanceService financeService;

    public List<Operation> getExpenses(@Nullable Period period,
                                       @Nullable List<Account> accounts,
                                       @Nullable List<Item> items) {
        SearchRequest request = formRequest(EXPENSE, period, accounts, items);
        return searchService.search(request);
    }

    public List<Operation> getTransfersToAccounts(@Nullable Period period, @Nullable List<Account> to) {
        SearchRequest request = formRequest(TRANSFER, period, null, to);
        return searchService.search(request).stream()
                .filter(transfer -> transferFilter((Transfer) transfer, to))
                .collect(Collectors.toList());
    }

    public List<Operation> getTransfersFromAccounts(@Nullable Period period, @Nullable List<Account> from) {
        SearchRequest request = formRequest(TRANSFER, period, from, null);
        return searchService.search(request).stream()
                .filter(transfer -> transferFilter((Transfer) transfer, from))
                .collect(Collectors.toList());
    }

    private boolean transferFilter(Transfer transfer, List<Account> accountList) {
        boolean transferWithinAccountList = accountList.contains(transfer.getTo())
                && accountList.contains(transfer.getFrom());
        return !transferWithinAccountList;
    }

    private SearchRequest formRequest(@NotNull FinanceOperationService.OperationType operationType,
                                      @Nullable com.katyshevtseva.date.Period period,
                                      @Nullable List<? extends OperationEnd> from,
                                      @Nullable List<? extends OperationEnd> to) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setOperationType(operationType);
        if (from != null)
            searchRequest.setFrom(from.stream().map(oe -> (OperationEnd) oe).collect(Collectors.toList()));
        if (to != null)
            searchRequest.setTo(to.stream().map(oe -> (OperationEnd) oe).collect(Collectors.toList()));
        searchRequest.setPeriod(period);
        return searchRequest;
    }
}
