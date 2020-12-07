package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;

import java.util.Date;

public class ExpensesSegment implements ReportSegment {
    private FinanceReportService reportService;
    private ItemHierarchyNode node;
    private int percent;
    private long amount;

    ExpensesSegment(FinanceReportService reportService, ItemHierarchyNode node, long amount) {
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
    public Report getChildReport(Date startDate, Date endDate) {
        if (!hasChildren())
            throw new RuntimeException("Попытка получить дочерний отчет у листа");

        return reportService.getReportByRoot(node, startDate, endDate);
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
