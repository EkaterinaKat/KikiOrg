package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static java.util.Comparator.*;

@RequiredArgsConstructor
@Service
public class HabitsService {
    private final HabitRepo habitRepo;
    private final HabitMarkService habitMarkService;
    private final HabitHistoryService habitHistoryService;

    public Habit saveHabit(Habit habit, String title, String desc, boolean active, boolean needToCreateDescChangeAction) {
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
        habit = habitRepo.save(habit);

        if (itIsCreation || (!desc.equals(oldDesc) && needToCreateDescChangeAction)) {
            habitHistoryService.commitAction(habit, desc, shiftDate(new Date(), DateUtils.TimeUnit.DAY, 1));
        }

        return habit;
    }

    public List<Habit> getAllHabits() {
        return habitRepo.findAll().stream()
                .sorted(nullsLast(comparing(Habit::isActive).reversed()
                        .thenComparing(habitMarkService::getFirstHabitMarkDateOrNull, nullsLast(naturalOrder()))))
                .collect(Collectors.toList());
    }

    public List<Habit> getActiveHabits() {
        return habitRepo.findByActiveTrue().stream()
                .sorted(nullsLast(comparing(habitMarkService::getFirstHabitMarkDateOrNull, nullsLast(naturalOrder()))))
                .collect(Collectors.toList());
    }

    public Habit getHabitById(Long id) {
        return habitRepo.findById(id).get();
    }
}
