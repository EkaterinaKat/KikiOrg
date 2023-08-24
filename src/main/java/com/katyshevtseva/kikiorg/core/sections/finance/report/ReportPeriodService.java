package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.kikiorg.core.CoreConstants.FINANCIAL_ACCOUNTING_START_DATE;

@Service
@RequiredArgsConstructor
public class ReportPeriodService {

    public ReportPeriod getAllTimePeriod() {
        return new ReportPeriod(new Period(FINANCIAL_ACCOUNTING_START_DATE, new Date()), "All time");
    }

    public List<ReportPeriod> getSeveralPastMonthsPeriods(int numOfLatestMonth) {
        List<ReportPeriod> periods = new ArrayList<>();

        Date date = DateUtils.shiftDate(new Date(), DateUtils.TimeUnit.MONTH, -1 * (numOfLatestMonth - 1));
        for (int i = 0; i < numOfLatestMonth; i++) {
            periods.add(new ReportPeriod(getPeriodOfMonthDateBelongsTo(date), getMonthYearString(date)));
            date = shiftDate(date, TimeUnit.MONTH, 1);
        }
        return periods;
    }

    @Data
    public static class ReportPeriod {
        private Period period;
        private final String title;

        public ReportPeriod(Period period, String title) {
            this.period = period;
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
