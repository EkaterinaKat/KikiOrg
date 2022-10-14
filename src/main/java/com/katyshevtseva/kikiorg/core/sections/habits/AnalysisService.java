package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
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
    private final Date someDaysAgo = shiftDate(new Date(), DAY, -49);
    private final HabitRepo habitRepo;
    private final HabitMarkService markService;
    private final StabilityCriterionService criterionService;

    public List<AnalysisResult> analyzeStabilityAndAssignNewStatusIfNeeded(List<Habit> habits) {
        return habits.stream()
                .map(habit -> {
                    StabilityCriterion stabilityCriterion = criterionService.getCriterionByHabitOrNull(habit);
                    AnalysisResult analysisResult = new AnalysisResult(habit, stabilityCriterion);
                    analysisResult.setStabilityCalculations(getStabilityCalculationsAndAssignNewStatusIfNeeded(habit));
                    return analysisResult;
                })
                .collect(Collectors.toList());
    }

    private String getStabilityCalculationsAndAssignNewStatusIfNeeded(Habit habit) {
        List<Date> dates = getDateRange(new Period(someDaysAgo, new Date()));
        StabilityCriterion criterion = criterionService.getCriterionByHabitOrNull(habit);
        int daysTotal = dates.size();
        int daysHabitDone = getDaysHabitDone(dates, habit);

        if (criterion == null) {
            return String.format("%d/%d. Критерии не заданы", daysHabitDone, daysTotal);
        }

        double actualRatio = (daysHabitDone * 1.0) / daysTotal;
        double minimalRatio = (criterion.getDaysHabitDone() * 1.0) / criterion.getDaysTotal();
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
        private final StabilityCriterion criterion;
        private String stabilityCalculations;

        public String getFullText() {
            return habit.getTitle()
                    + "\n" + stabilityCalculations
                    + "\n" + habit.getStabilityStatus()
                    + (criterion != null ? " " + criterion : "")
                    + "\n\n";
        }
    }
}
