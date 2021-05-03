package com.katyshevtseva.kikiorg.core.specials;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.CoreConstants;
import com.katyshevtseva.kikiorg.core.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.CalculationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ExpensesReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.IncomeReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.katyshevtseva.kikiorg.core.specials.TestConstants.ERROR_STRING;

@Component
@RequiredArgsConstructor
public class FinanceTest {
    private boolean success = true;
    private final CalculationService calculationService;
    private final ExpensesReportService expensesReportService;
    private final IncomeReportService incomeReportService;
    private final OwnerService ownerService;
    private final FinanceService financeService;
    private final AccountRepo accountRepo;

    //    @PostConstruct
    public void test() {
        Date start = new Date();

        System.out.println("\n");
        handleTestResult(validateAllAccounts());
        handleTestResult(testReportServices());

        if (success)
            System.out.println(TestConstants.BIG_SUCCESS_BANNER);
        else
            System.out.println("\nTest failed :(");

        Date end = new Date();
        System.out.println("time: " + (end.getTime() - start.getTime()));
    }

    private void handleTestResult(TestResult testResult) {
        System.out.println(testResult.getReport());
        if (!testResult.isSuccess())
            success = false;
    }

    private TestResult validateAllAccounts() {
        TestResult testResult = new TestResult();
        for (Account account : accountRepo.findAll()) {
            long calculatedAmount = calculationService.calculateAccountAmountByOperations(account);
            boolean amountsAreEqual = calculatedAmount == account.getAmount();
            testResult.addLineToReport(String.format("%s: %s", account.getTitle(),
                    (amountsAreEqual ? "Success" : ERROR_STRING) + String.format(
                            ": actual amount - %d, calculated amount - %d", account.getAmount(), calculatedAmount)));
            if (!amountsAreEqual)
                testResult.testFailed();
        }
        return testResult;
    }

    private TestResult testReportServices() {
        TestResult testResult = new TestResult();
        testResult.addLineToReport("Testing report services");
        for (Owner owner : Owner.values()) {
            ownerService.setCurrentOwner(owner);
            testResult.addLineToReport("User: " + owner);
            Report expensesReport = expensesReportService.getHeadReport(
                    new Period(CoreConstants.FINANCIAL_ACCOUNTING_START_DATE, new Date()));
            Report incomeReport = incomeReportService.getIncomeReport(
                    new Period(CoreConstants.FINANCIAL_ACCOUNTING_START_DATE, new Date()));
            testResult.addLineToReport(String.format(
                    "Total income: %d. Total expenses: %d.", incomeReport.getTotal(), expensesReport.getTotal()));
            long expectedAmount = incomeReport.getTotal() - expensesReport.getTotal();
            long actualAmount = getTotalAmountForCurrentUser();
            testResult.addLineToReport(String.format(
                    "Expected total amount: %d. Actual total amount: %d.", expectedAmount, actualAmount));
            testResult.addLineToReport(expectedAmount == actualAmount ? "Success" : ERROR_STRING);
            testResult.addLineToReport();
        }
        return testResult;
    }

    private long getTotalAmountForCurrentUser() {
        long amount = 0;
        for (Account account : financeService.getAccountsForCurrentUser()) {
            amount += account.getAmount();
        }
        return amount;
    }

    class TestResult {
        private String report = "";
        private boolean success = true;

        void addLineToReport(String s) {
            report += s + "\n";
        }

        void addLineToReport() {
            report += "\n";
        }

        void testFailed() {
            success = false;
        }

        public String getReport() {
            return report;
        }

        boolean isSuccess() {
            return success;
        }
    }
}
