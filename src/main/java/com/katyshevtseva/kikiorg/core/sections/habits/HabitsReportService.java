package com.katyshevtseva.kikiorg.core.sections.habits;


import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService.HabitMark;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.ReportCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorg.core.date.DateUtils.getDateRange;

@Service
public class HabitsReportService {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
    @Autowired
    private HabitsService habitsService;
    @Autowired
    private HabitMarkService habitMarkService;
    @Autowired
    private DateService dateService;

    public List<List<ReportCell>> getReport(List<Habit> habits, Period period) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead(habits));
        List<Date> dates = getDateRange(period);
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
        HabitMark mark = habitMarkService.getMarkOrNull(habit, date);
        if (mark == null)
            return ReportCell.empty();
        return ReportCell.filled(mark.getTextForReport());
    }

    private List<ReportCell> getReportHead(List<Habit> habits) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.empty());
        for (Habit habit : habits) {
            result.add(ReportCell.meta(habit.getTitle()));
        }
        return result;
    }
}
