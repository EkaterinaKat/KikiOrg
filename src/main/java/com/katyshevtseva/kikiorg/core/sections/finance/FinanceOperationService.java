package com.katyshevtseva.kikiorg.core.sections.finance;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.getLastMonthPeriod;
import static com.katyshevtseva.date.DateUtils.shiftDate;

@Service
@RequiredArgsConstructor
public class FinanceOperationService {
    private final FinanceService financeService;

    public List<Operation> getOperationsForLastMonth() {
        return getOperationsForPeriod(getLastMonthPeriod());
    }

    public List<Operation> getLastWeekOperations() {
        return getOperationsForPeriod(new Period(shiftDate(new Date(), DateUtils.TimeUnit.DAY, -7), new Date()));
    }

    private List<Operation> getOperationsForPeriod(Period period) {
        List<Operation> operations = new ArrayList<>();
        operations.addAll(financeService.getReplenishmentsByPeriod(period));
        operations.addAll(financeService.getExpensesByPeriod(period));
        operations.addAll(financeService.getTransfersByPeriod(period));
        operations.sort(Comparator.comparing(Operation::getDate).reversed());
        return operations;
    }

    public enum OperationType {
        TRANSFER, EXPENSE, REPLENISHMENT
    }

    public interface Operation {
        Date getDate();

        long getId();

        long getGoneAmount();

        long getCameAmount();

        String getFromTitle();

        String getToTitle();

        String getDateString();

        String getAmountString();

        OperationType getType();

        String getAdditionalInfo();
    }

}
