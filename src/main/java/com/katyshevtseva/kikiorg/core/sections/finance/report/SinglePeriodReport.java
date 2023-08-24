package com.katyshevtseva.kikiorg.core.sections.finance.report;

import lombok.Data;

@Data
public class SinglePeriodReport {
    private final SpodReport incomeReport;
    private final SpodReport outgoReport;

    public SinglePeriodReport(SpodReport incomeReport, SpodReport outgoReport) {
        this.incomeReport = incomeReport;
        this.outgoReport = outgoReport;
    }

    public Long getTotal() {
        return incomeReport.getTotal() - outgoReport.getTotal();
    }
}
