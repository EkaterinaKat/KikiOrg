package com.katyshevtseva.kikiorg.view.controller.finance.planning;

import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.ExpenseGroupingType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.PlanningService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanningUtil {
    private static final PlanningService planningService = Core.getInstance().planningService();

    public static List<List<ReportCell>> getReport(ExpenseGroupingType groupingType) {
        switch (groupingType) {
            case GROUP_BY_NECESSITY:
                return getGroupedReport(planningService.getCurrentMonthNecessityAmountMap());
            case GROUP_BY_ITEM:
                return getGroupedReport(planningService.getCurrentMonthItemAmountMap());
            case WITHOUT_GROUPING:
                return getWithoutGroupingReport(planningService.getCurrentMonthOperationsWithoutGrouping());
        }
        return null;
    }

    private static List<List<ReportCell>> getWithoutGroupingReport(List<Operation> operations) {
        List<List<ReportCell>> report = new ArrayList<>();
        for (Operation operation : operations) {
            List<ReportCell> reportLine = new ArrayList<>();

            reportLine.add(ReportCell.filled(operation.getDateString(), 120));
            reportLine.add(ReportCell.filled(operation.getGoneAmount() + "", 80));
            reportLine.add(ReportCell.filled(operation.getToTitle(), 160));
            reportLine.add(ReportCell.filled(operation.getAdditionalInfo(), 300));

            report.add(reportLine);
        }
        return report;
    }

    private static List<List<ReportCell>> getGroupedReport(Map<String, Integer> map) {
        List<List<ReportCell>> report = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            List<ReportCell> reportLine = new ArrayList<>();

            reportLine.add(ReportCell.filled(entry.getValue() + "", 120));
            reportLine.add(ReportCell.filled(entry.getKey(), 300));

            report.add(reportLine);
        }
        return report;
    }
}
