package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceReportService {
    @Autowired
    private FinanceService financeService;

    public String getReport() {
        String report = "";

        // Счета
        for (Account account : financeService.getAccounts()) {
            report += (" * " + account.getTitle() + ". " + account.getDescription() +
                    ". Владелец: " + account.getOwner() +
                    ". Amount: " + account.getAmount() + "\n");
        }

        // Доходы
        report += "\nДоходы \n";
        for (Replenishment replenishment : financeService.getReplenishments()) {
            report += (" * " + replenishment.getSource() + " " +
                    replenishment.getDateOfRepl() +
                    replenishment.getAccount().getTitle() + " " +
                    replenishment.getAmount() + "\n");
        }

        // Расходы
        report += "\nРасходы \n";
        for (Expense expense : financeService.getExpenses()) {
            report += (" * " + expense.getItem() + " " +
                    expense.getDateOfExp() + " " +
                    expense.getAccount().getTitle() + " " +
                    expense.getAmount() + "\n");
        }

        // Переводы
        report += "\nПереводы \n";
        for (Transfer transfer : financeService.getTransfers()) {
            report += (" * Transfer from: " + transfer.getFrom().getTitle() + " to: " +
                    transfer.getTo().getTitle() + ". " +
                    transfer.getDateEntity().getValue() + ". Amount: " +
                    transfer.getAmount() + "\n");
        }
        return report;
    }
}
