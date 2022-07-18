package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.HabitChangeActionRepo;
import com.katyshevtseva.kikiorg.core.repo.MarkRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.date.DateUtils.removeTimeFromDate;


@Service
@Transactional
@RequiredArgsConstructor
public class HabitMarkService {
    private final DateService dateService;
    private final MarkRepo markRepo;
    private final HabitChangeActionRepo habitChangeActionRepo;

    public void saveMarkOrRewriteIfExists(Habit habit, Date date, boolean markValue) throws Exception {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);

        Date dateOfFirstDesc = getDateOfFirstDesc(habit);
        if (removeTimeFromDate(date).before(dateOfFirstDesc)) {
            throw new Exception(String.format(
                    "Невозможно установить отметку на %s так как начало первого описания приходится на %s",
                    habit.getTitle(), READABLE_DATE_FORMAT.format(dateOfFirstDesc)));
        }

        markRepo.deleteByHabitAndDateEntity(habit, dateEntity);
        if (markValue) {
            Mark mark = new Mark();
            mark.setHabit(habit);
            mark.setDateEntity(dateEntity);
            markRepo.save(mark);
        }
    }

    public Date getDateOfFirstDesc(Habit habit) {
        return habitChangeActionRepo.findFirstByHabitOrderByDateEntityValueAsc(habit).get().getDateEntity().getValue();
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
        Page<Mark> page = markRepo.getMarksOrderedByDate(habit, PageRequest.of(0, 1));
        if (page.getTotalElements() == 0)
            return null;
        return page.getContent().get(0).getDateEntity().getValue();
    }
}
