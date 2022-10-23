package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.kikiorg.core.sections.finance.repo.TransferRepo;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FinanceMulticurrencyTest implements TestClass {
    private final TransferRepo transferRepo;

    public boolean test() {
        boolean success = true;

        //Если перевод одновалютный то суммы одинаковые обязательно
        List<Transfer> transfers = transferRepo.findAll();
        for (Transfer transfer : transfers) {
            if (transfer.getFrom().getCurrency() == transfer.getTo().getCurrency()) {
                if (!Objects.equals(transfer.getCameAmount(), transfer.getGoneAmount())) {
                    success = false;
                }
            }
        }

        return success;
    }

    @Override
    public String getTitle() {
        return "financeMulticurrencyTest";
    }
}
