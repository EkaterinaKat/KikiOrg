package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType;
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
        Map<String, Long> accountTitleAmountMap = new HashMap<>();
        for (Transfer transfer : transfers) {
            if (transfer.isOuter()) {
                String accountTitle = getAccountTitle(transfer, transferType);
                long initialAmount = accountTitleAmountMap.getOrDefault(accountTitle, 0L);
                long increasedAmount = initialAmount + transfer.getAmount();
                accountTitleAmountMap.put(accountTitle, increasedAmount);
            }
        }
        Report report = new Report("Transfers");
        for (Map.Entry<String, Long> entry : accountTitleAmountMap.entrySet()) {
            report.addSegment(new TransferSegment(
                    entry.getValue(),
                    entry.getKey(),
                    false, transferType,
                    this));
        }
        return report;
    }

    /* Если это отчет по переводам СО СЧЕТА тогда нас интересует имя счета, на который был совершен перевод.
     *  Если это отчет по переводам НА СЧЕТ тогда нас интересует имя счета, с которого был совершен перевод */
    private String getAccountTitle(Transfer transfer, TransferType transferType) {
        if (transferType == TransferType.FROM_USER_ACCOUNTS)
            return transfer.getTo().getTitleWithOwnerInfo();
        return transfer.getFrom().getTitleWithOwnerInfo();
    }
}
