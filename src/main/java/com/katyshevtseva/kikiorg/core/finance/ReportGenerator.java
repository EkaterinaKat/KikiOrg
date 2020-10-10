package com.katyshevtseva.kikiorg.core.finance;

import com.katyshevtseva.kikiorg.core.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.finance.entity.Replenishment;

import java.util.List;

class ReportGenerator {
    private FinanceManager financeManager;

    ReportGenerator(FinanceManager financeManager) {
        this.financeManager = financeManager;
    }

    String getReport() {
        String report = "";

        // Счета
        List<Account> accounts = financeManager.getAccounts();
        for (Account account : accounts) {
            report += (" * " + account.getTitle() + ". " + account.getDescription() + ". Amount: " + account.getAmount() + "\n");
        }

        // Доходы
        report += "\nДоходы \n";
        List<Replenishment> replenishments = financeManager.getReplenishments();
        for (Replenishment replenishment : replenishments) {
            report += (" * " + replenishment.getSource() + " " + replenishment.getDateOfRepl() + " " + replenishment.getAmount() + "\n");
        }

        // Расходы
        report += "\nРасходы \n";
        List<Expense> expenses = financeManager.getExpences();
        for (Expense expense : expenses) {
            report += (" * " + expense.getItem() + " " + expense.getDateOfExp() + " " + expense.getAmount() + "\n");
        }

        return report;
    }
}
