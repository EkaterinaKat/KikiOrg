package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class TransfersReportService {
    @Autowired
    private FinanceService financeService;

    TransferSegment getRootTransferSegment(Period period, TransferType transferType) {
        List<Transfer> transfers = financeService.getTransfersForCuByPeriod(period, transferType);
        long amount = 0;
        for (Transfer transfer : transfers) {
            if (transfer.isOuter())
                amount += transfer.getAmount();
        }
        return new TransferSegment(amount, "Transfers", true, transferType, this);
    }

    Report getTransfersReport(Period period, TransferType transferType) {
        List<Transfer> transfers = financeService.getTransfersForCuByPeriod(period, transferType);
        Map<Account, Long> accountAmountMap = new HashMap<>();
        for (Transfer transfer : transfers) {
            if (transfer.isOuter()) {
                long initialAmount = accountAmountMap.getOrDefault(transfer.getTo(), 0L);
                long increasedAmount = initialAmount + transfer.getAmount();
                accountAmountMap.put(transfer.getTo(), increasedAmount);
            }
        }
        Report report = new Report("Transfers");
        for (Map.Entry<Account, Long> entry : accountAmountMap.entrySet()) {
            report.addSegment(new TransferSegment(entry.getValue(), entry.getKey().getTitle(), false, transferType, this));
        }
        return report;
    }
}
