package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.history.HistoryService;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.HabitChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitChangeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class HabitHistoryService extends HistoryService<Habit, HabitChangeAction> {
    private final HabitChangeActionRepo habitChangeActionRepo;
    private final DateService dateService;

    @Override
    protected HabitChangeAction createNewAction() {
        return new HabitChangeAction();
    }

    @Override
    protected void saveNewAction(HabitChangeAction habitChangeAction) {
        habitChangeActionRepo.save(habitChangeAction);
    }

    @Override
    protected void setDate(HabitChangeAction habitChangeAction, Date date) {
        habitChangeAction.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
    }
}
