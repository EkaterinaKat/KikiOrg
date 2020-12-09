package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.kikiorg.core.sections.finance.FinanceService.TransferType.TO_USER_ACCOUNTS;

@Service
public class IncomeReportService {
    @Autowired
    private FinanceService financeService;
    @Autowired
    private TransfersReportService transfersReportService;

    public Report getIncomeReport(Period period) {
        Report report = new Report("Income");
        List<Replenishment> replenishments = financeService.getReplenishmentsForCuByPeriod(period);
        Map<Source, Long> sourceAmountMap = new HashMap<>();
        for (Replenishment replenishment : replenishments) {
            long initialAmount = sourceAmountMap.getOrDefault(replenishment.getSource(), 0L);
            long increasedAmount = initialAmount + replenishment.getAmount();
            sourceAmountMap.put(replenishment.getSource(), increasedAmount);
        }
        for (Map.Entry<Source, Long> entry : sourceAmountMap.entrySet()) {
            report.addSegment(new IncomeSegment(entry.getKey().getTitle(), entry.getValue()));
        }

        TransferSegment transferSegment = transfersReportService.getRootTransferSegment(period, TO_USER_ACCOUNTS);
        if (transferSegment.getAmount() > 0)
            report.addSegment(transferSegment);

        return report;
    }
}
