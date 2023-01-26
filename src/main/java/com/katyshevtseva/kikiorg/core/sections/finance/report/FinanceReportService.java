package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.OneInOneOutKnob;
import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.AccountGroupService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReport.Line;
import com.katyshevtseva.kikiorg.core.sections.finance.search.FinanceSearchService;
import com.katyshevtseva.kikiorg.core.sections.finance.search.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType.*;

@Service
@RequiredArgsConstructor
public class FinanceReportService {
    private final FinanceSearchService searchService;
    private final FinanceService financeService;
    private final ItemHierarchyService itemHierarchyService;

    public FullFinanceReport getReport(AccountGroup accountGroup, Period period) {
        try {
            AccountGroupService.validateAccountGroup(accountGroup.getAccounts());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new FullFinanceReport(
                getIncomeReport(accountGroup.getAccounts(), period),
                getOutgoReport(accountGroup.getAccounts(), period));
    }

    private FinanceReport getIncomeReport(List<Account> accounts, Period period) {
        FinanceReport incomeReport = new FinanceReport("Доход");
        addReplenishmentLinesToIncomeReport(incomeReport, accounts, period);
        addTransferLinesToIncomeReport(incomeReport, accounts, period);
        return incomeReport;
    }

    private FinanceReport getOutgoReport(List<Account> accounts, Period period) {
        FinanceReport outgoReport = new FinanceReport("Расход");
        addExpenseLinesToOutgoReport(outgoReport, accounts, period, itemHierarchyService.getTopLevelNodes());
        addTransferLinesToOutgoReport(outgoReport, accounts, period);
        return outgoReport;
    }

    private void addReplenishmentLinesToIncomeReport(FinanceReport report, List<Account> accounts, Period period) {
        SearchRequest request = formRequest(REPLENISHMENT, period, financeService.getAllSources(), accounts);
        report.addLines(getReportLinesByOperations(searchService.search(request), ReportType.INCOME));
    }

    private void addTransferLinesToIncomeReport(FinanceReport report, List<Account> accounts, Period period) {
        SearchRequest request = formRequest(TRANSFER, period, financeService.getAllAccounts(), accounts);

        List<Transfer> transfers = searchService.search(request).stream()
                .map(operation -> (Transfer) operation)
                .filter(transfer -> !accounts.contains(transfer.getFrom()))
                .collect(Collectors.toList());

        report.addLines(getReportLinesByOperations(transfers, ReportType.INCOME));
    }

    private void addExpenseLinesToOutgoReport(FinanceReport report, List<Account> accounts, Period period, List<HierarchyNode> hierarchyNodes) {
        for (HierarchyNode node : hierarchyNodes) {
            SearchRequest request = formRequest(EXPENSE, period, accounts,
                    itemHierarchyService.getAllDescendantLeavesByHierarchyNode(node));

            Long amount = searchService.search(request).stream()
                    .map(operation -> ((Expense) operation).getAmount())
                    .reduce(Long::sum).orElse(0L);

            report.addLine(node.getTitle(), amount);
        }
    }

    private void addTransferLinesToOutgoReport(FinanceReport report, List<Account> accounts, Period period) {
        SearchRequest request = formRequest(TRANSFER, period, accounts, financeService.getAllAccounts());

        List<Transfer> transfers = searchService.search(request).stream()
                .map(operation -> (Transfer) operation)
                .filter(transfer -> !accounts.contains(transfer.getTo()))
                .collect(Collectors.toList());

        report.addLines(getReportLinesByOperations(transfers, ReportType.OUTGO));
    }

    private List<Line> getReportLinesByOperations(List<? extends Operation> operations, ReportType reportType) {
        Map<String, Long> titleValueMap = new HashMap<>();
        for (Operation operation : operations) {
            String title = reportType.operationEndTitleSupplier.execute(operation);
            Long amount = reportType.operationAmountSupplier.execute(operation);
            Long total = titleValueMap.getOrDefault(title, 0L) + amount;
            titleValueMap.put(title, total);
        }
        return titleValueMap.entrySet().stream()
                .map(entry -> new Line(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private enum ReportType {
        INCOME(Operation::getFromTitle, Operation::getCameAmount),
        OUTGO(Operation::getToTitle, Operation::getGoneAmount);

        final OneInOneOutKnob<Operation, String> operationEndTitleSupplier;
        final OneInOneOutKnob<Operation, Long> operationAmountSupplier;

        ReportType(OneInOneOutKnob<Operation, String> operationEndTitleSupplier,
                   OneInOneOutKnob<Operation, Long> operationAmountSupplier) {
            this.operationEndTitleSupplier = operationEndTitleSupplier;
            this.operationAmountSupplier = operationAmountSupplier;
        }
    }

    private SearchRequest formRequest(OperationType operationType, Period period,
                                      List<? extends OperationEnd> from, List<? extends OperationEnd> to) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setOperationType(operationType);
        searchRequest.setFrom(from.stream().map(oe -> (OperationEnd) oe).collect(Collectors.toList()));
        searchRequest.setTo(to.stream().map(oe -> (OperationEnd) oe).collect(Collectors.toList()));
        searchRequest.setPeriod(period);
        return searchRequest;
    }
}
