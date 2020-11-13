package com.katyshevtseva.kikiorg.core.modes.finance;

import com.katyshevtseva.kikiorg.core.modes.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
import com.katyshevtseva.kikiorg.core.repo.AccountRepo;
import com.katyshevtseva.kikiorg.core.repo.CheckLineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceCheckService {
    @Autowired
    private CheckLineRepo checkLineRepo;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private AccountRepo accountRepo;

    public List<CheckLine> getCheckLine() {
        return checkLineRepo.findAllByOwner(financeService.getCurrentOwner());
    }

    public void rewriteCheckLine(List<CheckLine> checkLines) {
        checkLineRepo.deleteAll();
        checkLineRepo.saveAll(checkLines);
    }

    public String check(Account account, int amount) {
        long accountAmount = accountRepo.findById(account.getId()).get().getAmount();
        long diff = amount - accountAmount;
        return String.format("По расчетам: %s. По Факту: %s. Разница: %s.", accountAmount, amount, diff);
    }
}
