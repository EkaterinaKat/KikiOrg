package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.AccountAmountCalculationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ExpensesReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.IncomeReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class FinanceTest {
    private boolean success = true;
    @Autowired
    private AccountAmountCalculationService accountAmountCalculationService;
    @Autowired
    private ExpensesReportService expensesReportService;
    @Autowired
    private IncomeReportService incomeReportService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private AccountRepo accountRepo;

//    @PostConstruct
    public void test() {
        Date start = new Date();

        System.out.println("\n");
        handleTestResult(validateAllAccounts());
        handleTestResult(testReportServices());

        if (success)
            System.out.println("***********************************************\n" +
                    "                  SUCCESS\n" +
                    "***********************************************\n");
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
            long calculatedAmount = accountAmountCalculationService.calculateAccountAmountByOperations(account);
            boolean amountsAreEqual = calculatedAmount == account.getAmount();
            testResult.addLineToReport(String.format("%s: %s", account.getTitle(),
                    (amountsAreEqual ? "Success" : "Error") + String.format(
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
            Report expensesReport = expensesReportService.getHeadReport(DateUtils.getAllTimePeriod());
            Report incomeReport = incomeReportService.getIncomeReport(DateUtils.getAllTimePeriod());
            testResult.addLineToReport(String.format(
                    "Total income: %d. Total expenses: %d.", incomeReport.getTotal(), expensesReport.getTotal()));
            long expectedAmount = incomeReport.getTotal() - expensesReport.getTotal();
            long actualAmount = getTotalAmountForCurrentUser();
            testResult.addLineToReport(String.format(
                    "Expected total amount: %d. Actual total amount: %d.", expectedAmount, actualAmount));
            testResult.addLineToReport(expectedAmount == actualAmount ? "Success" : "Error");
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
}
