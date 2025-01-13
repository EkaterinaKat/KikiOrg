package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.Period;
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
import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.general.ReportCell.Type.HEAD_COLUMN;
import static com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService.someDaysAgo;

@Service
@RequiredArgsConstructor
public class HabitsReportService {
    private final HabitsService habitsService;
    private final HabitMarkService habitMarkService;

    public List<List<ReportCell>> getQuickReport(Date dateToHighlight) {
        return getReport(habitsService.getActiveHabits(),
                new Period(someDaysAgo, new Date()), dateToHighlight);
    }

    public List<List<ReportCell>> getReport(List<Habit> habits, Period period, Date dateToHighlight) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead(habits));
        List<Date> dates = getDateRange(period);
        Collections.reverse(dates);  // Чтобы последние даты были наверху таблицы
        for (Date date : dates) {
            result.add(getReportLine(date, habits, dateToHighlight));
        }
        return result;
    }

    private List<ReportCell> getReportLine(Date date, List<Habit> habits, Date dateToHighlight) {
        List<ReportCell> result = new ArrayList<>();
        ReportCell.ReportCellBuilder dateCellBuilder = ReportCell.builder()
                .text(READABLE_DATE_FORMAT.format(date))
                .width(100);

        if (dateToHighlight != null) {
            dateCellBuilder.color(equalsIgnoreTime(date, dateToHighlight) ? ORANGE.getCode() : WHITE.getCode());
        }

        result.add(dateCellBuilder.build());
        for (Habit habit : habits) {
            result.add(convertToCell(habit, date));
        }
        return result;
    }

    private ReportCell convertToCell(Habit habit, Date date) {
        Mark mark = habitMarkService.getMarkOrNull(habit, date);
        if (mark == null)
            return ReportCell.builder().build();
        return ReportCell.builder().color(GREEN.getCode()).build();
    }

    private List<ReportCell> getReportHead(List<Habit> habits) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().build());
        for (Habit habit : habits) {
            result.add(ReportCell.builder().type(HEAD_COLUMN).text(habit.getTitle()).build());
        }
        return result;
    }
}
