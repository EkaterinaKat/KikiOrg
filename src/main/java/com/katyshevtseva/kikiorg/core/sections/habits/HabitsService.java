package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitChangeAction;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.HabitChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.HabitRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

@RequiredArgsConstructor
@Service
public class HabitsService {
    private final HabitRepo habitRepo;
    private final HabitHistoryService habitHistoryService;
    private final HabitChangeActionRepo habitChangeActionRepo;

    @Transactional
    public Habit saveHabit(Habit habit, String title, String desc, int criterionDaysDone, int criterionDaysTotal,
                           boolean active, boolean needToCreateDescChangeAction) {
        boolean itIsCreation = false;
        String oldDesc = null;
        if (habit == null) {
            habit = new Habit();
            itIsCreation = true;
        } else {
            oldDesc = habit.getDescription();
        }
        habit.setTitle(title);
        habit.setDescription(desc);
        habit.setActive(active);
        habit.setCriterionDaysTotal(criterionDaysTotal);
        habit.setCriterionDaysDone(criterionDaysDone);
        habit = habitRepo.save(habit);

        Date date = shiftDate(new Date(), DateUtils.TimeUnit.DAY, 1);
        if (itIsCreation) {
            habitHistoryService.commitAction(habit, desc, date);
        } else if (!desc.equals(oldDesc)) {
            if (needToCreateDescChangeAction) {
                habitChangeActionRepo.removeByHabitAndDateEntityValue(habit, date);
                habitHistoryService.commitAction(habit, desc, date);
            } else {
                HabitChangeAction changeAction = habitChangeActionRepo.findFirstByHabitOrderByDateEntityValueDesc(habit).get();
                changeAction.setDescription(desc);
                habitChangeActionRepo.save(changeAction);
            }
        }

        return habit;
    }

    public List<Habit> getAllHabits() {
        return habitRepo.findAll().stream()
                .sorted(nullsLast(comparing(Habit::isActive).reversed()
                        .thenComparing(Habit::getTitle)))
                .collect(Collectors.toList());
    }

    public List<Habit> getActiveHabits() {
        return habitRepo.findByActiveTrue().stream()
                .sorted(comparing(Habit::getTitle))
                .collect(Collectors.toList());
    }

    public Habit getHabitById(Long id) {
        return habitRepo.findById(id).get();
    }
}
