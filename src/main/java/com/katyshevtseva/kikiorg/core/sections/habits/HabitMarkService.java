package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.MarkRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional
@RequiredArgsConstructor
public class HabitMarkService {
    private final DateService dateService;
    private final MarkRepo markRepo;

    public void saveMarkOrRewriteIfExists(Habit habit, Date date, boolean markValue) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);

        markRepo.deleteByHabitAndDateEntity(habit, dateEntity);
        if (markValue) {
            Mark mark = new Mark();
            mark.setHabit(habit);
            mark.setDateEntity(dateEntity);
            markRepo.save(mark);
        }
    }

    // Без модификатора public екзешник не работает
    public Mark getMarkOrNull(Habit habit, Date date) {
        DateEntity dateEntity = dateService.getDateEntityIfExistsOrNull(date);

        if (dateEntity != null) {
            return markRepo.findByHabitAndDateEntity(habit, dateEntity).orElse(null);
        }
        return null;
    }

    // Без модификатора public екзешник не работает
    public Date getFirstHabitMarkDateOrNull(Habit habit) {
        Page<Mark> page = markRepo.getOrderedByDateMarks(habit, PageRequest.of(0, 1));
        if (page.getTotalElements() == 0)
            return null;
        return page.getContent().get(0).getDateEntity().getValue();
    }
}
