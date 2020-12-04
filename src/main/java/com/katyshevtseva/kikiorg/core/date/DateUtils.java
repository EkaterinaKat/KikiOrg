package com.katyshevtseva.kikiorg.core.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {

    public static Date getMonthAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    public static List<Date> getDateRange(Date start, Date end) {
        Date date = new Date(start.getTime());
        Date oneDayAfterEnd = addOneDay(end);

        List<Date> result = new ArrayList<>();
        while (date.before(oneDayAfterEnd)) {
            result.add(date);
            date = addOneDay(date);
        }
        return result;
    }

    private static Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
