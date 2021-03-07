package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class AnalysisService {
    @Autowired
    private HabitMarkService markService;

    public AnalysisResult analyzeHabit(Habit habit, Period period) {
        List<Date> dates = DateUtils.getDateRange(period);
        int daysHabitDone = 0;
        for (Date date : dates) {
            if (markService.getMarkOrNull(habit, date) != null)
                daysHabitDone++;
        }
        return new AnalysisResult(habit, dates.size(), daysHabitDone);
    }

    public class AnalysisResult {
        private Habit habit;
        private int daysTotal;
        private int daysHabitDone;

        AnalysisResult(Habit habit, int daysTotal, int daysHabitDone) {
            this.habit = habit;
            this.daysTotal = daysTotal;
            this.daysHabitDone = daysHabitDone;
        }
    }
}
