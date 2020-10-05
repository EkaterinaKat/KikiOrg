package com.katyshevtseva.kikiorg.core.finance;

import com.katyshevtseva.kikiorg.core.finance.entity.Account;

import java.util.List;

class ReportGenerator {
    private FinanceManager financeManager;

    ReportGenerator(FinanceManager financeManager) {
        this.financeManager = financeManager;
    }

    String getReport(){
        String report = "";
        List<Account> accounts = financeManager.getAccounts();
        for (Account account: accounts){
            report+=(" * "+account.getTitle()+". "+account.getDescription()+". Amount: "+account.getAmount()+"\n");
        }
        return report;
    }
}
