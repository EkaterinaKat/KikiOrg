package com.katyshevtseva.kikiorg.core.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
    private static Date startingPoint;

    static {
        try {
            startingPoint = new SimpleDateFormat("dd.MM.yyyy").parse("09.09.2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static Date getMonthAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    private static Date getWeekAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }

    public static Period getLastWeekPeriod() {
        return new Period(getWeekAgoDate(), new Date());
    }

    public static List<Date> getDateRange(Period period) {
        Date date = new Date(period.start().getTime());
        Date oneDayAfterEnd = addOneDay(period.end());

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

    public static Period getAllTimePeriod() {
        return new Period(startingPoint, new Date());
    }
}
