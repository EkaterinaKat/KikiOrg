package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.ReportPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceReportService {

    public void getReport(List<Account> accounts, ReportPeriod reportPeriod) {
        System.out.println(accounts);
        System.out.println(reportPeriod);
    }

}
