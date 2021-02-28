package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.repo.HabitUnionRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitUnionService {
    @Autowired
    private HabitUnionRepo habitUnionRepo;

    public List<HabitUnion> getAllHabitUnions() {
        return habitUnionRepo.findAll();
    }

    public HabitUnion createUnion(String title, HabitGroup habitGroup) {
        HabitUnion habitUnion = new HabitUnion();
        habitUnion.setTitle(title);
        habitUnion.setHabitGroup(habitGroup);
        habitUnion.setActive(true);
        return habitUnionRepo.save(habitUnion);
    }

    public void addHabitToUnion(HabitUnion habitUnion, Habit habit) {
        habitUnion.getHabits().add(habit);
        habitUnionRepo.save(habitUnion);
    }

    public void removeHabitFromUnion(HabitUnion habitUnion, Habit habit) {
        habitUnion.getHabits().remove(habit);
        habitUnionRepo.save(habitUnion);
    }

    public void deleteHabitUnion(HabitUnion habitUnion) {
        habitUnionRepo.delete(habitUnion);
    }

    public void saveEditedUnion(HabitUnion habitUnion) {
        habitUnionRepo.save(habitUnion);
    }
}
