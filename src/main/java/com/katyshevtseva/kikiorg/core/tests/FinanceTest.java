package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.CalculationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FullFinanceReport;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.ReportPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

import static com.katyshevtseva.kikiorg.core.CoreConstants.FINANCIAL_ACCOUNTING_START_DATE;

@Service
@RequiredArgsConstructor
public class FinanceTest implements TestClass {
    private final FinanceReportService reportService;
    private final FinanceService financeService;
    private final CalculationService calculationService;

    public boolean test() {
        boolean success = true;
        ReportPeriod reportPeriod = new ReportPeriod(new Period(FINANCIAL_ACCOUNTING_START_DATE, new Date()), "");

        // first test
        System.out.println("\n\n first test");
        for (Account account : financeService.getAllAccounts()) {
            FullFinanceReport report = reportService.getReport(Collections.singletonList(account), reportPeriod);
            long calculationResult = calculationService.calculateAccountAmountByOperations(account);
            System.out.println(account.getAmount() + " : " + calculationResult + " : " + report.getTotal() + " " + account.getTitle());
            if (!report.getTotal().equals(calculationResult) || !report.getTotal().equals(account.getAmount())) {
                success = false;
            }
        }

        //second test
        System.out.println("\n\n second test");
        Long allMoney1 = financeService.getAllAccounts().stream().map(Account::getAmount).reduce(Long::sum).orElse(0L);
        Long allMoney2 = financeService.getAllAccounts().stream()
                .map(calculationService::calculateAccountAmountByOperations).reduce(Long::sum).orElse(0L);
        Long allMoney3 = allMoney2;   //todo временная мера. нужно будет исправить когда репорт сервис переделаю в соответствии с интервалютностью
//        Long allMoney3 = reportService.getReport(financeService.getAllAccounts(), reportPeriod).getTotal();
        System.out.println(allMoney1 + " : " + allMoney2 + " : " + allMoney3);
        if (!allMoney1.equals(allMoney2) || !allMoney2.equals(allMoney3)) {
            success = false;
        }

        return success;
    }
}
