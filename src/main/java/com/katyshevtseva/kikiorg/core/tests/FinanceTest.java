package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.CalculationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.SinglePeriodReport;
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
    private final TransferRepo transferRepo;

    public boolean test() {
        boolean success = true;
        Period period = new Period(FINANCIAL_ACCOUNTING_START_DATE, new Date());

        // first test
        System.out.println("\n\n first test");
        for (Account account : financeService.getAllAccounts()) {
            SinglePeriodReport report = reportService.getReport(
                    new AccountGroup("", Collections.singletonList(account)), period);

            long calculationResult = calculationService.calculateAccountAmountByOperations(account);
            System.out.println(account.getAmount()
                    + " : " + calculationResult
                    + " : " + report.getTotal()
                    + " " + account.getTitleWithAdditionalInfo());
            if (!report.getTotal().equals(calculationResult) || !report.getTotal().equals(account.getAmount())) {
                success = false;
            }
        }

        System.out.println("\n\n transfer:from!=to test");
        for (Transfer transfer : transferRepo.findAll()) {
            if (transfer.getFrom().equals(transfer.getTo())) {
                System.out.println("transfer.getFrom().equals(transfer.getTo())");
                success = false;
            }
        }

        return success;
    }

    @Override
    public String getTitle() {
        return "financeTest";
    }
}
