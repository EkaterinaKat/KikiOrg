package com.katyshevtseva.kikiorg.core.date;

import java.util.Date;

public class Period {
    private Date startDate;
    private Date endDate;

    public Period(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period getLastMonth() {
        return new Period(DateUtils.getMonthAgoDate(), new Date());
    }

    public Date start() {
        return startDate;
    }

    public Date end() {
        return endDate;
    }
}
