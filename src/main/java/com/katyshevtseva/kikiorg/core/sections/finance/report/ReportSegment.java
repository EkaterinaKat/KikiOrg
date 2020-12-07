package com.katyshevtseva.kikiorg.core.sections.finance.report;

import java.util.Date;

public interface ReportSegment {
    long getAmount();

    void setPercent(int percent);

    String getTitle();

    int getPercent();

    boolean hasChildren();

    Report getChildReport(Date startDate, Date endDate);
}
