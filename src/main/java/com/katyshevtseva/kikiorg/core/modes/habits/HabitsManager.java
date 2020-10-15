package com.katyshevtseva.kikiorg.core.modes.habits;

import com.katyshevtseva.kikiorg.core.repo.HabitsRepo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitsManager implements InitializingBean {
    private static HabitsManager INSTANCE;
    @Autowired
    private HabitsRepo habitsRepo;

    public static HabitsManager getInstance() {
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

    public List<Habit> getAllHabits(){
        return habitsRepo.findAll();
    }
}
