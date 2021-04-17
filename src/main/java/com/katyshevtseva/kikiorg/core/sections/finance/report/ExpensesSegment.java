package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;


public class ExpensesSegment implements ReportSegment {
    private ExpensesReportService reportService;
    private ItemHierarchyNode node;
    private int percent;
    private long amount;

    ExpensesSegment(ExpensesReportService reportService, ItemHierarchyNode node, long amount) {
        this.reportService = reportService;
        this.node = node;
        this.amount = amount;
    }

    public ItemHierarchyNode getNode() {
        return node;
    }

    @Override
    public boolean hasChildren() {
        return !node.isLeaf();
    }

    @Override
    public Report getChildReport(Period period) {
        if (!hasChildren())
            throw new RuntimeException("Попытка получить дочерний отчет у листа");

        return reportService.getReportByRoot(node, period);
    }

    public int getPercent() {
        return percent;
    }

    public long getAmount() {
        return amount;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getTitle() {
        return node.getTitle();
    }
}
