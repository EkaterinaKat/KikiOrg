package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;

public class ExpensesSegment implements ReportSegment {
    private ItemHierarchyService.ItemHierarchyNode node;
    private int percent;
    private long amount;

    ExpensesSegment(ItemHierarchyService.ItemHierarchyNode node, long amount) {
        this.node = node;
        this.amount = amount;
    }

    public ItemHierarchyService.ItemHierarchyNode getNode() {
        return node;
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
