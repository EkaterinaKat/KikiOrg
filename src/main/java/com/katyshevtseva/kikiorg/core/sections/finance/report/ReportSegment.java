package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;

public interface ReportSegment {
    long getAmount();

    void setPercent(int percent);

    String getTitle();

    int getPercent();

    ItemHierarchyNode getNode();
}
