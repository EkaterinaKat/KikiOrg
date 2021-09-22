package com.katyshevtseva.kikiorg.core.sections.finance.report;

import lombok.Data;

@Data
public class FullFinanceReport {
    private final FinanceReport incomeReport;
    private final FinanceReport outgoReport;

    public FullFinanceReport(FinanceReport incomeReport, FinanceReport outgoReport) {
        this.incomeReport = incomeReport;
        this.outgoReport = outgoReport;
    }

    public Long getTotal() {
        return incomeReport.getTotal() - outgoReport.getTotal();
    }
}
