package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService.NUM_OF_STABILITY_DAYS;

@Service
@RequiredArgsConstructor
public class HabitsReportService {
    private final HabitsService habitsService;
    private final HabitMarkService habitMarkService;

    public List<List<ReportCell>> getQuickReport() {
        return getReport(habitsService.getActiveHabits(),
                new Period(shiftDate(new Date(), TimeUnit.DAY, -(NUM_OF_STABILITY_DAYS - 1)), new Date()));
    }

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
        result.add(ReportCell.filled(READABLE_DATE_FORMAT.format(date), Styler.StandardColor.WHITE, 100));
        for (Habit habit : habits) {
            result.add(convertToCell(habit, date));
        }
        return result;
    }

    private ReportCell convertToCell(Habit habit, Date date) {
        Mark mark = habitMarkService.getMarkOrNull(habit, date);
        if (mark == null)
            return ReportCell.empty();
        return ReportCell.filled("", Styler.StandardColor.GREEN);
    }

    private List<ReportCell> getReportHead(List<Habit> habits) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.empty());
        for (Habit habit : habits) {
            result.add(ReportCell.columnHead(habit.getTitle()));
        }
        return result;
    }
}
