package com.katyshevtseva.kikiorg.core.modes.habits;


import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.HabitMark;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.ReportCell;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HabitsReportService implements InitializingBean {
    private static HabitsReportService INSTANCE;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
    @Autowired
    private HabitsService habitsService;
    @Autowired
    private HabitMarkConverter habitMarkConverter;

    public static HabitsReportService getInstance() {
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
        List<Date> dates = getDateRange(startDate, endDate);
        Collections.reverse(dates);  // Чтобы последние даты были наверху таблицы
        for (Date date : dates) {
            result.add(getReportLine(date, habits));
        }
        return result;
    }

    private List<ReportCell> getReportLine(Date date, List<Habit> habits) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.meta(dateFormat.format(date)));
        for (Habit habit : habits) {
            result.add(convertToCell(habit, date));
        }
        return result;
    }

    private ReportCell convertToCell(Habit habit, Date date) {
        HabitMark mark = habitsService.getMarkOrNull(habit, date);
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
