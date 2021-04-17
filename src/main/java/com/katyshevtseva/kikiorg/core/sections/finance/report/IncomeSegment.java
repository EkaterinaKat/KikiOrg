package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.Period;

public class IncomeSegment implements ReportSegment {
    private long amount;
    private int percent;
    private String title;

    IncomeSegment(String title, long amount) {
        this.amount = amount;
        this.title = title;
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
        return false;
    }

    @Override
    public Report getChildReport(Period period) {
        throw new RuntimeException("Попытка получить дочерний отчет у сегмента дохода");
    }
}
