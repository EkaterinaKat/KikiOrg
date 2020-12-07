package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.Period;


public interface ReportSegment {
    long getAmount();

    void setPercent(int percent);

    String getTitle();

    int getPercent();

    boolean hasChildren();

    Report getChildReport(Period period);
}
