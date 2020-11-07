package com.katyshevtseva.kikiorg.core.modes.finance;

import com.katyshevtseva.kikiorg.core.modes.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Replenishment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceReportService {
    @Autowired
    private FinanceService financeService;

    public String getReport() {
        String report = "";

        // Счета
        List<Account> accounts = financeService.getAccounts();
        for (Account account : accounts) {
            report += (" * " + account.getTitle() + ". " +
                    account.getDescription() + ". Amount: " +
                    account.getAmount() + "\n");
        }

        // Доходы
        report += "\nДоходы \n";
        List<Replenishment> replenishments = financeService.getReplenishments();
        for (Replenishment replenishment : replenishments) {
            report += (" * " + replenishment.getSource() + " " +
                    replenishment.getDateOfRepl() +
                    replenishment.getAccount().getTitle() + " " +
                    replenishment.getAmount() + "\n");
        }

        // Расходы
        report += "\nРасходы \n";
        List<Expense> expenses = financeService.getExpenses();
        for (Expense expense : expenses) {
            report += (" * " + expense.getItem() + " " +
                    expense.getDateOfExp() + " " +
                    expense.getAccount().getTitle() + " " +
                    expense.getAmount() + "\n");
        }

        return report;
    }
}
