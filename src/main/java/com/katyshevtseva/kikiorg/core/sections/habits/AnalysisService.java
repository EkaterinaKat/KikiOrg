package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.HabitRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.TimeUnit.DAY;
import static com.katyshevtseva.date.DateUtils.getDateRange;
import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus.*;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    public final static int NUM_OF_STABILITY_DAYS = 30;
    private final Date someDaysAgo = shiftDate(new Date(), DAY, -(NUM_OF_STABILITY_DAYS-1));
    private final HabitRepo habitRepo;
    private final HabitMarkService markService;

    public List<AnalysisResult> analyzeStabilityAndAssignNewStatusIfNeeded(List<Habit> habits) {
        return habits.stream()
                .map(habit -> {
                    AnalysisResult analysisResult = new AnalysisResult(habit);
                    analysisResult.setStabilityCalculations(getStabilityCalculationsAndAssignNewStatusIfNeeded(habit));
                    return analysisResult;
                })
                .collect(Collectors.toList());
    }

    private String getStabilityCalculationsAndAssignNewStatusIfNeeded(Habit habit) {
        List<Date> dates = getDateRange(new Period(someDaysAgo, new Date()));
        int daysTotal = dates.size();
        int daysHabitDone = getDaysHabitDone(dates, habit);

        if (!habit.hasCriterion()) {
            return String.format("%d/%d. Критерии не заданы", daysHabitDone, daysTotal);
        }

        double actualRatio = (daysHabitDone * 1.0) / daysTotal;
        double minimalRatio = (habit.getCriterionDaysDone() * 1.0) / habit.getCriterionDaysTotal();
        boolean isStable = Double.compare(actualRatio, minimalRatio) >= 0;

        assignNewStatusIfNeeded(habit, isStable);

        return String.format("%d/%d -> %d/%d",
                daysHabitDone,
                daysTotal,
                (int) Math.ceil(minimalRatio * daysTotal),
                daysTotal);
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
        habitRepo.save(habit);
    }

    private int getDaysHabitDone(List<Date> dates, Habit habit) {
        int daysHabitDone = 0;
        for (Date date : dates) {
            if (markService.getMarkOrNull(habit, date) != null)
                daysHabitDone++;
        }
        return daysHabitDone;
    }

    @Data
    @RequiredArgsConstructor
    public static class AnalysisResult {
        private final Habit habit;
        private String stabilityCalculations;

        public String getFullText() {
            return habit.getTitle()
                    + "\n" + stabilityCalculations
                    + "\n" + habit.getStabilityStatus()
                    + (habit.hasCriterion() ? " " + habit.getCriterionString() : "")
                    + "\n\n";
        }
    }
}
