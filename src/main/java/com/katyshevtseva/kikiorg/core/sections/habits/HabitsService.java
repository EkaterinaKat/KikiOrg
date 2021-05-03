package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.DescriptionRepo;
import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Description;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.shiftDate;

@RequiredArgsConstructor
@Service
public class HabitsService {
    private final HabitRepo habitRepo;
    private final EnumElementRepo enumElementRepo;
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

    public void saveEnumElement(EnumElement enumElement) {
        enumElementRepo.save(enumElement);
    }

    public List<EnumElement> getEnumElementsByHabit(Habit habit) {
        List<EnumElement> enumElements = enumElementRepo.findByHabit(habit);
        enumElements.add(getEmptyEnumElement());
        return enumElements;
    }

    private EnumElement getEmptyEnumElement() {
        EnumElement enumElement = new EnumElement();
        enumElement.setTitle("-");
        enumElement.setId(-1L);
        return enumElement;
    }

    public Habit getHabitById(Long id) {
        return habitRepo.findById(id).get();
    }

    public void newHabitDesc(Habit habit, String newDescText, boolean createNewDesc) {
        if (habit.getCurrentDescription() != null && !createNewDesc) {
            Description existingDescription = habit.getCurrentDescription();
            existingDescription.setText(newDescText);
            descriptionRepo.save(existingDescription);
            return;
        }

        Description newDescription = new Description();
        newDescription.setBeginningDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        newDescription.setText(newDescText);
        newDescription.setHabit(habit);
        descriptionRepo.save(newDescription);

        if (habit.getCurrentDescription() != null) {
            Description oldDescription = habit.getCurrentDescription();
            oldDescription.setEndDate(dateService.createIfNotExistAndGetDateEntity(
                    shiftDate(new Date(), DateUtils.TimeUnit.DAY, -1)));
            descriptionRepo.save(oldDescription);
        }

        habit.setCurrentDescription(newDescription);
        habitRepo.save(habit);
    }
}
