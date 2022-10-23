package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.finance.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepo transferRepo;
    private final DateService dateService;
    private final FinanceService financeService;

    public boolean isIntercurrencyTransfer(Account account1, Account account2) {
        if (account1 == null || account2 == null) {
            return false;
        }
        return account1.getCurrency() != account2.getCurrency();
    }

    public void addTransfer(Account from, Account to, Long amountGone, Long amountCame, Date date) {
        if (!isIntercurrencyTransfer(from, to) && !amountCame.equals(amountGone)) {
            throw new RuntimeException();
        }

        saveTransfer(from, to, amountGone, amountCame, date);
    }

    @Transactional
    void saveTransfer(Account from, Account to, Long amountGone, Long amountCame, Date date) {
        Transfer transfer = new Transfer(from, to, amountGone, amountCame, dateService.createIfNotExistAndGetDateEntity(date));
        transferRepo.saveAndFlush(transfer);

        financeService.addToAccountAmount(from, (-1) * amountGone);
        financeService.addToAccountAmount(to, amountCame);
    }
}
