package com.katyshevtseva.kikiorg.core.polarperiod;

import com.katyshevtseva.date.DateUtils;

import java.util.Date;

import static com.katyshevtseva.date.DateUtils.shiftDate;

public class PeriodUtils {

    public static boolean isOverdue(PolarPeriod period) {
        return DateUtils.removeTimeFromDate(new Date()).after(getEndDate(period));
    }

    private static Date getEndDate(PolarPeriod period) {
        Date startDate = period.getStart().getValue();
        int numOfUnits = period.getPeriod();

        switch (period.getTimeUnit()) {
            case DAY:
                return shiftDate(startDate, DateUtils.TimeUnit.DAY, numOfUnits);
            case WEEK:
                return shiftDate(startDate, DateUtils.TimeUnit.DAY, numOfUnits * 7);
            case MONTH:
                return shiftDate(startDate, DateUtils.TimeUnit.MONTH, numOfUnits);
            case YEAR:
                return shiftDate(startDate, DateUtils.TimeUnit.YEAR, numOfUnits);
        }
        throw new RuntimeException();
    }
}
