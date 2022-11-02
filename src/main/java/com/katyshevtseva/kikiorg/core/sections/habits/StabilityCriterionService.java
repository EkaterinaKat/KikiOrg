package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.sections.habits.repo.StabilityCriterionRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StabilityCriterionService {
    private final StabilityCriterionRepo stabilityCriterionRepo;

    public void saveOrRewriteCriterion(Habit habit, int daysTotal, int daysHabitDone) {
        stabilityCriterionRepo.deleteByHabit(habit);

        StabilityCriterion criterion = new StabilityCriterion();
        criterion.setHabit(habit);
        criterion.setDaysTotal(daysTotal);
        criterion.setDaysHabitDone(daysHabitDone);
        stabilityCriterionRepo.save(criterion);
    }

    public StabilityCriterion getCriterionByHabitOrNull(Habit habit) {
        return stabilityCriterionRepo.findByHabit(habit).orElse(null);
    }
}
