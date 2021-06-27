package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.DescriptionRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Description;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.shiftDate;

@RequiredArgsConstructor
@Service
public class HabitsService {
    private final HabitRepo habitRepo;
    private final DescriptionRepo descriptionRepo;
    private final DateService dateService;

    public void saveHabit(Habit habit) {
        habitRepo.save(habit);
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = habitRepo.findAll();
        habits.sort(Comparator.comparing(Habit::isActive).reversed());
        return habits;
    }

    public List<Habit> getActiveHabits() {
        return habitRepo.findByActiveTrue();
    }

    public Habit getHabitById(Long id) {
        return habitRepo.findById(id).get();
    }

    public List<Description> getAllHabitDescriptions(Habit habit) {
        return descriptionRepo.findByHabit(habit).stream().sorted(
                Comparator.comparing(o -> ((Description) o).getBeginningDate().getValue()).reversed()).collect(Collectors.toList());
    }

    public void newHabitDesc(Habit habit, String newDescText, boolean createNewDesc) {
        if (habit.getCurrentDescription() != null && !createNewDesc) {
            Description existingDescription = habit.getCurrentDescription();
            existingDescription.setText(newDescText);
            descriptionRepo.save(existingDescription);
            return;
        }

        Description newDescription = new Description();
        newDescription.setBeginningDate(dateService.createIfNotExistAndGetDateEntity(
                shiftDate(new Date(), DateUtils.TimeUnit.DAY, 1)));
        newDescription.setText(newDescText);
        newDescription.setHabit(habit);
        descriptionRepo.save(newDescription);

        if (habit.getCurrentDescription() != null) {
            Description oldDescription = habit.getCurrentDescription();
            oldDescription.setEndDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
            descriptionRepo.save(oldDescription);
        }

        habit.setCurrentDescription(newDescription);
        habitRepo.save(habit);
    }
}
