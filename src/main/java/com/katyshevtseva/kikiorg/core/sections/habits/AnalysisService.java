package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AnalysisService {
    @Autowired
    private HabitMarkService markService;
    @Autowired
    private StabilityCriterionService criterionService;

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
        private String shortResult;
        private String stabilityInfo;
        private boolean isStable;

        AnalysisResult(Habit habit, int daysTotal, int daysHabitDone) {
            shortResult = String.format("%s: %d/%d", habit.getTitle(), daysHabitDone, daysTotal);
            analyzeStability(habit, daysTotal, daysHabitDone);
        }

        private void analyzeStability(Habit habit, int daysTotal, int daysHabitDone) {
            StabilityCriterion criterion = criterionService.getCriterionByHabitOrNull(habit);

            if (criterion == null) {
                stabilityInfo = shortResult + ". Критерии не заданы";
                return;
            }

            double actualRatio = (daysHabitDone * 1.0) / daysTotal;
            double minimalRatio = (criterion.getDaysHabitDone() * 1.0) / criterion.getDaysTotal();
            isStable = Double.compare(actualRatio, minimalRatio) >= 0;

            stabilityInfo = String.format("%s. %s. НО: %d/%d=%.3f. МО: %d/%d=%.3f.", habit.getTitle(),
                    isStable ? "Стабильно" : "Не стабильно", daysHabitDone, daysTotal, actualRatio,
                    criterion.getDaysHabitDone(), criterion.getDaysTotal(), minimalRatio);
        }

        public String getShortResult() {
            return shortResult;
        }

        public String getStabilityInfo() {
            return stabilityInfo;
        }

        public boolean isStable() {
            return isStable;
        }
    }
}
