package com.katyshevtseva.kikiorg.core.sections.finance.report;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorg.core.CoreConstants.FINANCIAL_ACCOUNTING_START_DATE;

@Service
@RequiredArgsConstructor
public class ReportPeriodService {

    public List<ReportPeriod> getReportPeriods() {
        List<ReportPeriod> periods = new ArrayList<>(getAllPastMonthsReportPeriods(true));
        periods.add(new ReportPeriod());
        periods.add(new ReportPeriod(new Period(FINANCIAL_ACCOUNTING_START_DATE, new Date()), "All time"));
        Collections.reverse(periods);
        return periods;
    }

    List<ReportPeriod> getAllPastMonthsReportPeriods(boolean includeLastMonth) {
        List<ReportPeriod> periods = new ArrayList<>();

        Date date = DateUtils.getNextMonthFirstDate(FINANCIAL_ACCOUNTING_START_DATE);
        while (true) {
            Date monthFirstDate = new Date(date.getTime());
            date = DateUtils.shiftDate(date, DateUtils.TimeUnit.MONTH, 1);
            Date monthLastDate = DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, -1);
            ReportPeriod period = new ReportPeriod(new Period(monthFirstDate, monthLastDate), DateUtils.getMonthYearString(monthFirstDate));
            boolean itIsLastMonth = monthLastDate.after(new Date());

            if (itIsLastMonth) {
                if (includeLastMonth) {
                    periods.add(period);
                }
                break;
            } else {
                periods.add(period);
            }
        }

        return periods;
    }

    public static class ReportPeriod {
        private Period period;
        private final String title;
        private final boolean averageMonthly;

        public ReportPeriod(Period period, String title) {
            this.period = period;
            this.title = title;
            averageMonthly = false;
        }

        public ReportPeriod() {
            title = "Среднемесячный отчет";
            averageMonthly = true;
        }

        @Override
        public String toString() {
            return title;
        }

        public Period getPeriod() {
            return period;
        }

        public String getTitle() {
            return title;
        }

        public boolean isAverageMonthly() {
            return averageMonthly;
        }
    }
}
