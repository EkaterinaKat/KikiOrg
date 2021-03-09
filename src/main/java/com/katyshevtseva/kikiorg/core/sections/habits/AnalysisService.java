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

@Service
public class AnalysisService {
    private final Date yesterday = shiftDate(new Date(), DAY, -1);
    private final Date threeMonthAgo = shiftDate(yesterday, MONTH, -3);
    @Autowired
    private HabitMarkService markService;
    @Autowired
    private StabilityCriterionService criterionService;

    public String simpleAnalyze(Habit habit, Period period) {
        List<Date> dates = DateUtils.getDateRange(period);
        int daysHabitDone = 0;
        for (Date date : dates) {
            if (markService.getMarkOrNull(habit, date) != null)
                daysHabitDone++;
        }
        return String.format("%s: %d/%d", habit.getTitle(), daysHabitDone, dates.size());
    }

    public AnalysisResult stabilityAnalyze(Habit habit) {
        List<Date> dates = DateUtils.getDateRange(new Period(threeMonthAgo, yesterday));
        StabilityCriterion criterion = criterionService.getCriterionByHabitOrNull(habit);
        int daysTotal = dates.size();
        int daysHabitDone = getDaysHabitDone(dates, habit);

        if (criterion == null) {
            String stabilityInfo = String.format("%s: %d/%d. Критерии не заданы", habit.getTitle(), daysHabitDone, daysTotal);
            return new AnalysisResult(stabilityInfo, false);
        }

        double actualRatio = (daysHabitDone * 1.0) / daysTotal;
        double minimalRatio = (criterion.getDaysHabitDone() * 1.0) / criterion.getDaysTotal();
        boolean isStable = Double.compare(actualRatio, minimalRatio) >= 0;

        String stabilityInfo = String.format("%s. %s. НО: %d/%d=%.3f. МО: %d/%d=%.3f.", habit.getTitle(),
                isStable ? "Стабильно" : "Не стабильно", daysHabitDone, daysTotal, actualRatio,
                criterion.getDaysHabitDone(), criterion.getDaysTotal(), minimalRatio);
        return new AnalysisResult(stabilityInfo, isStable);
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
        private String stabilityInfo;
        private boolean isStable;

        AnalysisResult(String stabilityInfo, boolean isStable) {
            this.stabilityInfo = stabilityInfo;
            this.isStable = isStable;
        }

        public String getStabilityInfo() {
            return stabilityInfo;
        }

        public boolean isStable() {
            return isStable;
        }
    }
}
