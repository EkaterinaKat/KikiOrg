package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceSearchService.OperationEnd;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.TimeUnit.DAY;
import static com.katyshevtseva.general.GeneralUtils.isEmpty;
import static com.katyshevtseva.kikiorg.core.CoreConstants.FINANCIAL_ACCOUNTING_START_DATE;

@Getter
public class SearchRequest {
    private Period period;
    private long minAmount;
    private long maxAmount;
    private OperationType operationType;
    private List<OperationEnd> from;
    private List<OperationEnd> to;

    public void setPeriod(LocalDate start, LocalDate end) {
        this.period = new Period(
                start != null ? java.sql.Date.valueOf(start) : FINANCIAL_ACCOUNTING_START_DATE,
                end != null ? java.sql.Date.valueOf(end) : DateUtils.shiftDate(new Date(), DAY, 1)
        );
    }

    public void setMinAmount(String minAmountString) {
        this.minAmount = isEmpty(minAmountString) ? 0 : Long.parseLong(minAmountString);
    }

    public void setMaxAmount(String maxAmountString) {
        this.maxAmount = isEmpty(maxAmountString) ? Long.MAX_VALUE : Long.parseLong(maxAmountString);
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public void setFrom(List<OperationEnd> from, List<OperationEnd> fullList) {
        this.from = from.isEmpty() ? fullList : from;
    }

    public void setTo(List<OperationEnd> to, List<OperationEnd> fullList) {
        this.to = to.isEmpty() ? fullList : to;
    }
}
