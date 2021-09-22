package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.*;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.*;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.ReportPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceReportService {
    private final FinanceSearchService searchService;
    private final FinanceService financeService;
    private final ItemHierarchyService itemHierarchyService;
    private final ReportPeriodService reportPeriodService;

    public FullFinanceReport getReport(List<Account> accounts, ReportPeriod reportPeriod) {
        if (accounts == null || accounts.isEmpty() || reportPeriod == null) {
            throw new RuntimeException();
        }

        if (reportPeriod.isAverageMonthly()) {
            return getAverageMonthlyReport(accounts);
        }

        return new FullFinanceReport(
                getIncomeReport(accounts, reportPeriod.getPeriod()),
                getOutgoReport(accounts, reportPeriod.getPeriod()));
    }

    public FullFinanceReport getAverageMonthlyReport(List<Account> accounts) {
        List<Period> periods = reportPeriodService.getAllPastMonthsReportPeriods()
                .stream().map(ReportPeriod::getPeriod).collect(Collectors.toList());

        List<FinanceReport> incomeReports = periods.stream()
                .map(period -> getIncomeReport(accounts, period)).collect(Collectors.toList());

        List<FinanceReport> outgoReports = periods.stream()
                .map(period -> getOutgoReport(accounts, period)).collect(Collectors.toList());

        return new FullFinanceReport(getAverageReport(incomeReports), getAverageReport(outgoReports));
    }

    private FinanceReport getAverageReport(List<FinanceReport> reports) {
        Map<String, Long> titleAmountSumMap = new HashMap<>();
        for (FinanceReport report : reports) {
            for (FinanceReport.Line line : report.getLines()) {
                titleAmountSumMap.put(line.getTitle(), titleAmountSumMap.getOrDefault(line.getTitle(), 0L) + line.getValue());
            }
        }
        FinanceReport report = new FinanceReport("Average report");
        for (Map.Entry<String, Long> entry : titleAmountSumMap.entrySet()) {
            report.addLine(entry.getKey(), entry.getValue() / reports.size());
        }
        return report;
    }

    public FinanceReport getSubreport(List<Account> accounts, ReportPeriod reportPeriod, ItemGroup itemGroup) {
        FinanceReport outgoReport = new FinanceReport(itemGroup.getTitle());
        addExpenseLinesToOutgoReport(outgoReport, accounts, reportPeriod.getPeriod(), itemHierarchyService.getNodesByParent(itemGroup));
        return outgoReport;
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
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setOperationType(FinanceOperationService.OperationType.REPLENISHMENT);
        searchRequest.setTo(accounts.stream().map(account -> (OperationEnd) account).collect(Collectors.toList()));
        searchRequest.setFrom(financeService.getAllSources().stream().map(source -> (OperationEnd) source).collect(Collectors.toList()));
        searchRequest.setPeriod(period);
        List<Replenishment> replenishments = searchService.search(searchRequest).stream()
                .map(operation -> (Replenishment) operation).collect(Collectors.toList());

        Map<String, Long> titleValueMap = new HashMap<>();
        for (Replenishment replenishment : replenishments) {
            String lineTitle = replenishment.getSource().getTitle();
            titleValueMap.put(lineTitle, titleValueMap.getOrDefault(lineTitle, 0L) + replenishment.getAmount());
        }
        for (Map.Entry<String, Long> entry : titleValueMap.entrySet()) {
            report.addLine(entry.getKey(), entry.getValue());
        }
    }

    private void addTransferLinesToIncomeReport(FinanceReport report, List<Account> accounts, Period period) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setOperationType(FinanceOperationService.OperationType.TRANSFER);
        searchRequest.setTo(accounts.stream().map(account -> (OperationEnd) account).collect(Collectors.toList()));
        searchRequest.setFrom(financeService.getAllAccounts().stream().map(account -> (OperationEnd) account).collect(Collectors.toList()));
        searchRequest.setPeriod(period);
        List<Transfer> transfers = searchService.search(searchRequest).stream()
                .map(operation -> (Transfer) operation)
                .filter(transfer -> !accounts.contains(transfer.getFrom()))
                .collect(Collectors.toList());

        Map<String, Long> titleValueMap = new HashMap<>();
        for (Transfer transfer : transfers) {
            String lineTitle = "tr: " + transfer.getFrom().getTitle();
            titleValueMap.put(lineTitle, titleValueMap.getOrDefault(lineTitle, 0L) + transfer.getAmount());
        }
        for (Map.Entry<String, Long> entry : titleValueMap.entrySet()) {
            report.addLine(entry.getKey(), entry.getValue());
        }
    }

    private void addExpenseLinesToOutgoReport(FinanceReport report, List<Account> accounts, Period period, List<ItemHierarchyNode> hierarchyNodes) {
        for (ItemHierarchyNode node : hierarchyNodes) {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setOperationType(FinanceOperationService.OperationType.EXPENSE);
            searchRequest.setFrom(accounts.stream().map(account -> (OperationEnd) account).collect(Collectors.toList()));
            searchRequest.setTo(itemHierarchyService.getAllDescendantItemsByHierarchyNode(node)
                    .stream().map(item -> (OperationEnd) item).collect(Collectors.toList()));
            searchRequest.setPeriod(period);
            Long amount = searchService.search(searchRequest).stream()
                    .map(operation -> ((Expense) operation).getAmount())
                    .reduce(Long::sum).orElse(0L);

            report.addLine(node.getTitle(), amount, node.isLeaf() ? null : (ItemGroup) node);
        }
    }

    private void addTransferLinesToOutgoReport(FinanceReport report, List<Account> accounts, Period period) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setOperationType(FinanceOperationService.OperationType.TRANSFER);
        searchRequest.setFrom(accounts.stream().map(account -> (OperationEnd) account).collect(Collectors.toList()));
        searchRequest.setTo(financeService.getAllAccounts().stream().map(account -> (OperationEnd) account).collect(Collectors.toList()));
        searchRequest.setPeriod(period);
        List<Transfer> transfers = searchService.search(searchRequest).stream()
                .map(operation -> (Transfer) operation)
                .filter(transfer -> !accounts.contains(transfer.getTo()))
                .collect(Collectors.toList());

        Map<String, Long> titleValueMap = new HashMap<>();
        for (Transfer transfer : transfers) {
            String lineTitle = "tr: " + transfer.getTo().getTitle();
            titleValueMap.put(lineTitle, titleValueMap.getOrDefault(lineTitle, 0L) + transfer.getAmount());
        }
        for (Map.Entry<String, Long> entry : titleValueMap.entrySet()) {
            report.addLine(entry.getKey(), entry.getValue());
        }
    }
}
