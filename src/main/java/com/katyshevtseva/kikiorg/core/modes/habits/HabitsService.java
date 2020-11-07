package com.katyshevtseva.kikiorg.core.modes.habits;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.HabitMark;
import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitMarkRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitsRepo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HabitsService implements InitializingBean {
    private static HabitsService INSTANCE;
    @Autowired
    private HabitsRepo habitsRepo;
    @Autowired
    private EnumElementRepo enumElementRepo;
    @Autowired
    private HabitMarkRepo habitMarkRepo;
    @Autowired
    private DateService dateService;

    public static HabitsService getInstance() {
        while (INSTANCE == null) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }

    public void saveHabit(Habit habit) {
        habitsRepo.save(habit);
    }

    public List<Habit> getAllHabits() {
        return habitsRepo.findAll();
    }

    public List<Habit> getActiveHabits() {
        return habitsRepo.findByActiveTrue();
    }

    public void saveEnumElement(EnumElement enumElement) {
        enumElementRepo.save(enumElement);
    }

    public List<EnumElement> getEnumElementsByHabit(Habit habit) {
        return enumElementRepo.findByHabitId(habit.getId());
    }

    public void makeMark(Habit habit, Date date, Object mark) {
        HabitMark habitMark = new HabitMark();
        habitMark.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
        habitMark.setHabit(habit);
        habitMark.setMark(HabitMarkConverter.prepareToPersist(habit, mark));
        habitMarkRepo.save(habitMark);
    }

    HabitMark getMarkOrNull(Habit habit, Date date) {
        return habitMarkRepo.findByHabitIdAndDateEntityId(
                habit.getId(), dateService.createIfNotExistAndGetDateEntity(date).getId()).orElse(null);
    }
}
