package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitsRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class HabitsService {
    @Autowired
    private HabitsRepo habitsRepo;
    @Autowired
    private EnumElementRepo enumElementRepo;

    public void saveHabit(Habit habit) {
        habitsRepo.save(habit);
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = habitsRepo.findAll();
        habits.sort(Comparator.comparing(Habit::isActive).reversed());
        return habits;
    }

    public List<Habit> getActiveHabits() {
        return habitsRepo.findByActiveTrue();
    }

    public void saveEnumElement(EnumElement enumElement) {
        enumElementRepo.save(enumElement);
    }

    public List<EnumElement> getEnumElementsByHabit(Habit habit) {
        return enumElementRepo.findByHabit(habit);
    }

    public Habit getHabitById(Long id) {
        return habitsRepo.findById(id).get();
    }
}
