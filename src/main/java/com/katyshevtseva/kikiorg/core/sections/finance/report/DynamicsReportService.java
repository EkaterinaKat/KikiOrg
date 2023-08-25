package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.sections.finance.AccountGroupService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.ReportPeriod;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicsReportService {
    private final ReportPeriodService periodService;
    private final FinanceReportService financeReportService;

    public List<DrElement> getReport(AccountGroup accountGroup) throws Exception {
        AccountGroupService.validateAccountGroup(accountGroup.getAccounts());

        List<ReportPeriod> periods = periodService.getSeveralPastMonthsPeriods(10);
        List<DrElement> elements = new ArrayList<>();

        for (ReportPeriod period : periods) {
            DrElement element = new DrElement(accountGroup);
            element.setPeriod(period);

            SpodReport incomeReport = financeReportService.getIncomeReport(accountGroup.getAccounts(), period.getPeriod());
            element.setIncome(incomeReport.getTotal());

            SpodReport outgoReport = financeReportService.getOutgoReport(accountGroup.getAccounts(), period.getPeriod());
            element.setOutcome(outgoReport.getTotal());

            elements.add(element);
        }

        return elements;
    }

    @Data
    public static class DrElement {
        private AccountGroup accountGroup;
        private ReportPeriod period;
        private long income;
        private long outcome;

        public DrElement(AccountGroup accountGroup) {
            this.accountGroup = accountGroup;
        }
    }
}
