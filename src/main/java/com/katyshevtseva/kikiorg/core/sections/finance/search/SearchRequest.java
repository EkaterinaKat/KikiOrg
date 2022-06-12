package com.katyshevtseva.kikiorg.core.sections.finance.search;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.general.GeneralUtils.isEmpty;

@Getter
public class SearchRequest {
    private Date start;
    private Date end;
    private Long minAmount;
    private Long maxAmount;
    private OperationType operationType;
    private List<OperationEnd> from;
    private List<OperationEnd> to;

    public void setStart(LocalDate start) {
        if (start != null)
            this.start = java.sql.Date.valueOf(start);
    }

    public void setEnd(LocalDate end) {
        if (end != null)
            this.end = java.sql.Date.valueOf(end);
    }

    public void setPeriod(Period period) {
        if (period != null) {
            start = period.start();
            end = period.end();
        }
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

    public void setFrom(List<OperationEnd> from) {
        if (!isEmpty(from)) {
            this.from = from;
        }
    }

    public void setTo(List<OperationEnd> to) {
        if (!isEmpty(to)) {
            this.to = to;
        }
    }
}
