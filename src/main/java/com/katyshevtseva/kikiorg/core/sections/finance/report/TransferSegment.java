package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.Period;

public class TransferSegment implements ReportSegment {
    private long amount;
    private String title;
    private int percent;
    private boolean hasChildren;
    private FinanceReportService reportService;

    TransferSegment(long amount, String title, boolean hasChildren, FinanceReportService reportService) {
        this.amount = amount;
        this.title = title;
        this.hasChildren = hasChildren;
        this.reportService = reportService;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public void setPercent(int percent) {
        this.percent = percent;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getPercent() {
        return percent;
    }

    @Override
    public boolean hasChildren() {
        return hasChildren;
    }

    @Override
    public Report getChildReport(Period period) {
        if (!hasChildren())
            throw new RuntimeException("Попытка получить дочерний отчет у листа");

        return reportService.getTransfersReport(period);
    }
}
