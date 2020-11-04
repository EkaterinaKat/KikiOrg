package com.katyshevtseva.kikiorg.core.modes.habits;


import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.HabitMark;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.ReportCell;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HabitsReportMaker implements InitializingBean {
    private static HabitsReportMaker INSTANCE;
    @Autowired
    private HabitsManager habitsManager;
    @Autowired
    private HabitMarkConverter habitMarkConverter;

    public static HabitsReportMaker getInstance() {
        while (INSTANCE == null) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }


    public List<List<ReportCell>> getReport(List<Habit> habits, Date startDate, Date endDate) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead(habits));
        for (Date date : getDateRange(startDate, endDate)) {
            result.add(getReportLine(date, habits));
        }
        return result;
    }

    private List<ReportCell> getReportLine(Date date, List<Habit> habits) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.meta(date.toString()));
        for (Habit habit : habits) {
            result.add(convertToCell(habit, date));
        }
        return result;
    }

    private ReportCell convertToCell(Habit habit, Date date) {
        HabitMark mark = habitsManager.getMark(habit, date);
        if (mark == null)
            return ReportCell.empty();
        return habitMarkConverter.prepareForReport(habit, mark);
    }

    private List<ReportCell> getReportHead(List<Habit> habits) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.empty());
        for (Habit habit : habits) {
            result.add(ReportCell.meta(habit.getTitle()));
        }
        return result;
    }

    private List<Date> getDateRange(Date start, Date end) {
        Date date = new Date(start.getTime());
        Date oneDayAfterEnd = addOneDay(end);

        List<Date> result = new ArrayList<>();
        while (date.before(oneDayAfterEnd)) {
            result.add(date);
            date = addOneDay(date);
        }
        return result;
    }

    private Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

}
