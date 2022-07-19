package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.repo.MarkRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.TimeUnit.DAY;
import static com.katyshevtseva.date.DateUtils.TimeUnit.MONTH;
import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus.*;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final Date yesterday = shiftDate(new Date(), DAY, -1);
    private final Date threeMonthAgo = shiftDate(yesterday, MONTH, -3);
    private final HabitRepo habitRepo;
    private final HabitMarkService markService;
    private final MarkRepo markRepo;
    private final StabilityCriterionService criterionService;
    private final UninterruptedPeriodService upService;

    public List<AnalysisResult> analyzeStabilityAndAssignNewStatusIfNeeded(List<Habit> habits) {
        habits = upService.orderByLengthOfCurrentUp(habits);
        return habits.stream()
                .map(habit -> {
                    StabilityCriterion stabilityCriterion = criterionService.getCriterionByHabitOrNull(habit);
                    AnalysisResult analysisResult = new AnalysisResult(habit, stabilityCriterion);
                    analysisResult.setStabilityCalculations(getStabilityCalculationsAndAssignNewStatusIfNeeded(habit));
                    analysisResult.setDoDoNotRelation(getDoDoNotRelation(habit));
                    analysisResult.setUpInfo(getUpInfo(habit));
                    return analysisResult;
                })
                .collect(Collectors.toList());
    }

    private String getUpInfo(Habit habit) {
        Period mostLongUP = upService.getMostLongUpOrNull(habit);
        return String.format("Current UP: %s\nMost long UP: %s",
                upService.getLengthOfCurrentUp(habit),
                mostLongUP != null ? getPeriodStringWithLengthIncludingBorders(mostLongUP) : "-");
    }

    private String getDoDoNotRelation(Habit habit) {
        Date firstMarkDate = markService.getFirstMarkDateOrNull(habit);

        if (firstMarkDate==null) {
            return "Do/Don't -";
        }

        int allNum = getNumberOfDays(firstMarkDate, new Date());
        int doNum = (int) markRepo.countByHabit(habit);
        int doNotNum = allNum - doNum;
        return String.format("Do/Don't = %d/%d = %.3f", doNum, doNotNum, (doNum * 1.0 / doNotNum));
    }

    private String getStabilityCalculationsAndAssignNewStatusIfNeeded(Habit habit) {
        List<Date> dates = getDateRange(new Period(threeMonthAgo, yesterday));
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

        return String.format("Наст: %d/%d = %.3f.\nМин: %d/%d = %.3f",
                daysHabitDone,
                daysTotal,
                actualRatio,
                (int) Math.ceil(minimalRatio * daysTotal),
                daysTotal,
                minimalRatio);
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
        private String upInfo;
        private String doDoNotRelation;

        public String getFullText() {
            return habit.getTitle()
                    + " " + habit.getStabilityStatus()
                    + (criterion != null ? " " + criterion : "")
                    + "\n" + stabilityCalculations
                    + "\n" + upInfo
                    + "\n" + doDoNotRelation
                    + "\n\n";
        }
    }
}
