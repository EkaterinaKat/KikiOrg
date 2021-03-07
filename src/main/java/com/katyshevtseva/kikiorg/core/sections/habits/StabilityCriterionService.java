package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.repo.StabilityCriterionRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StabilityCriterionService {
    @Autowired
    private StabilityCriterionRepo stabilityCriterionRepo;

    public void saveOrRevriteCriterion(Habit habit, int daysTotal, int daysHabitDone) {
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
