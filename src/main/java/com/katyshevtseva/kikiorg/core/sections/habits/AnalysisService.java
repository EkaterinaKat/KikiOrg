package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.Utils.TimeUnit.DAY;
import static com.katyshevtseva.date.Utils.TimeUnit.MONTH;
import static com.katyshevtseva.date.Utils.shiftDate;
import static com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus.*;

@Service
public class AnalysisService {
    private final Date yesterday = shiftDate(new Date(), DAY, -1);
    private final Date threeMonthAgo = shiftDate(yesterday, MONTH, -3);
    @Autowired
    private HabitMarkService markService;
    @Autowired
    private StabilityCriterionService criterionService;
    @Autowired
    private HabitsService habitsService;

    public String simpleAnalyze(Habit habit, Period period) {
        List<Date> dates = DateUtils.getDateRange(period);
        int daysHabitDone = 0;
        for (Date date : dates) {
            if (markService.getMarkOrNull(habit, date) != null)
                daysHabitDone++;
        }
        return String.format("%s: %d/%d", habit.getTitle(), daysHabitDone, dates.size());
    }

    public AnalysisResult analyzeStabilityAndAssignNewStatusIfNeeded(Habit habit) {
        List<Date> dates = DateUtils.getDateRange(new Period(threeMonthAgo, yesterday));
        StabilityCriterion criterion = criterionService.getCriterionByHabitOrNull(habit);
        int daysTotal = dates.size();
        int daysHabitDone = getDaysHabitDone(dates, habit);

        if (criterion == null) {
            return new AnalysisResult(habit,
                    String.format("%d/%d. Критерии не заданы", daysHabitDone, daysTotal));
        }

        double actualRatio = (daysHabitDone * 1.0) / daysTotal;
        double minimalRatio = (criterion.getDaysHabitDone() * 1.0) / criterion.getDaysTotal();
        boolean isStable = Double.compare(actualRatio, minimalRatio) >= 0;

        assignNewStatusIfNeeded(habit, isStable);

        return new AnalysisResult(habit, String.format("НО: %d/%d=%.3f. МО: %d/%d=%.3f.", daysHabitDone, daysTotal,
                actualRatio, criterion.getDaysHabitDone(), criterion.getDaysTotal(), minimalRatio));
    }

    private void assignNewStatusIfNeeded(Habit habit, boolean isStable) {
        if (isStable)
            habit.setStabilityStatus(STABLE);
        else {
            if (habit.getStabilityStatus() == null)
                habit.setStabilityStatus(NOT_STABLE);
            if (habit.getStabilityStatus() == STABLE)
                habit.setStabilityStatus(STABILITY_LOST);
        }
        habitsService.saveHabit(habit);
    }

    private int getDaysHabitDone(List<Date> dates, Habit habit) {
        int daysHabitDone = 0;
        for (Date date : dates) {
            if (markService.getMarkOrNull(habit, date) != null)
                daysHabitDone++;
        }
        return daysHabitDone;
    }

    public class AnalysisResult {
        private Habit habit;
        private String calculations;

        AnalysisResult(Habit habit, String calculations) {
            this.habit = habit;
            this.calculations = calculations;
        }

        public String getHabitTitle() {
            return habit.getTitle();
        }

        public String getStatus() {
            return habit.getStabilityStatus().toString();
        }

        public String getCalculations() {
            return calculations;
        }
    }
}
