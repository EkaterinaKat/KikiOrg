package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.TimeUnit.DAY;
import static com.katyshevtseva.general.GeneralUtils.isEmpty;
import static com.katyshevtseva.kikiorg.core.CoreConstants.FINANCIAL_ACCOUNTING_START_DATE;

@Getter
public class SearchRequest {
    private Date start = FINANCIAL_ACCOUNTING_START_DATE;
    private Date end = DateUtils.shiftDate(new Date(), DAY, 1);
    private long minAmount = 0;
    private long maxAmount = Long.MAX_VALUE;
    private OperationType operationType;
    private List<OperationEnd> from = new ArrayList<>();
    private List<OperationEnd> to = new ArrayList<>();

    public void setStart(LocalDate start) {
        if (start != null)
            this.start = java.sql.Date.valueOf(start);
    }

    public void setEnd(LocalDate end) {
        if (end != null)
            this.end = java.sql.Date.valueOf(end);
    }

    public void setPeriod(Period period) {
        start = period.start();
        end = period.end();
    }

    public void setMinAmount(String minAmountString) {
        if (!isEmpty(minAmountString))
            minAmount = Long.parseLong(minAmountString);
    }

    public void setMaxAmount(String maxAmountString) {
        if (!isEmpty(maxAmountString))
            maxAmount = Long.parseLong(maxAmountString);
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

    public void setFrom(List<OperationEnd> from) {
        if (from == null || from.isEmpty()) {
            throw new RuntimeException();
        }

        this.from = from;
    }

    public void setTo(List<OperationEnd> to) {
        if (to == null || to.isEmpty()) {
            throw new RuntimeException();
        }

        this.to = to;
    }
}
