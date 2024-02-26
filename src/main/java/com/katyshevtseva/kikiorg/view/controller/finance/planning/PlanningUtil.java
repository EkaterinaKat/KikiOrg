package com.katyshevtseva.kikiorg.view.controller.finance.planning;

import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.ExpenseGroupingType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.PlanningService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.PotentialExpense;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanningUtil {
    private static final PlanningService planningService = Core.getInstance().planningService();

    public static List<List<ReportCell>> getReport(ExpenseGroupingType groupingType,
                                                   OneArgKnob<PotentialExpense> peDeleteKnob) {
        switch (groupingType) {
            case GROUP_BY_NECESSITY:
                return getGroupedReport(planningService.getCurrentMonthNecessityAmountMap());
            case GROUP_BY_ITEM:
                return getGroupedReport(planningService.getCurrentMonthItemAmountMap());
            case WITHOUT_GROUPING:
                return getWithoutGroupingReport(planningService.getCurrentMonthOperationsWithoutGrouping(), peDeleteKnob);
        }
        throw new RuntimeException();
    }

    private static List<List<ReportCell>> getWithoutGroupingReport(
            List<Operation> operations, OneArgKnob<PotentialExpense> peDeleteKnob) {
        List<List<ReportCell>> report = new ArrayList<>();
        for (Operation operation : operations) {
            List<ReportCell> reportLine = new ArrayList<>();

            reportLine.add(ReportCell.builder().text(operation.getDateString()).width(120).build());
            reportLine.add(ReportCell.builder().text(operation.getGoneAmount() + "").width(80).build());
            reportLine.add(ReportCell.builder().text(operation.getToTitle()).width(160)
                    .contextMenu(getContextMenu(operation, peDeleteKnob)).build());
            reportLine.add(ReportCell.builder().text(operation.getAdditionalInfo()).width(300).build());

            report.add(reportLine);
        }
        return report;
    }

    private static ContextMenu getContextMenu(Operation operation, OneArgKnob<PotentialExpense> peDeleteKnob) {
        if (!(operation instanceof PotentialExpense))
            return null;

        ContextMenu contextMenu = new ContextMenu();

        MenuItem item = new MenuItem("Delete");
        item.setOnAction(event1 -> {
            peDeleteKnob.execute((PotentialExpense) operation);
        });
        contextMenu.getItems().add(item);

        return contextMenu;
    }

    private static List<List<ReportCell>> getGroupedReport(Map<String, Integer> map) {
        List<List<ReportCell>> report = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            List<ReportCell> reportLine = new ArrayList<>();

            reportLine.add(ReportCell.builder().text(entry.getValue() + "").width(120).build());
            reportLine.add(ReportCell.builder().text(entry.getKey()).width(300).build());

            report.add(reportLine);
        }
        return report;
    }
}
