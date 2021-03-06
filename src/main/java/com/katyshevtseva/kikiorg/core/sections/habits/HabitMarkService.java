package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.repo.BooleanMarkRepo;
import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import com.katyshevtseva.kikiorg.core.repo.EnumMarkRepo;
import com.katyshevtseva.kikiorg.core.repo.NumMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class HabitMarkService {
    @Autowired
    private EnumElementRepo enumElementRepo;
    @Autowired
    private DateService dateService;
    @Autowired
    private NumMarkRepo numMarkRepo;
    @Autowired
    private EnumMarkRepo enumMarkRepo;
    @Autowired
    private BooleanMarkRepo booleanMarkRepo;

    public void saveMarkOrRewriteIfExists(Habit habit, Date date, Object markValue) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);

        switch (habit.getType()) {
            case number:
                numMarkRepo.deleteByHabitAndDateEntity(habit, dateEntity);
                Integer value = (Integer) markValue;
                if (value != 0) {
                    NumMark numMark = new NumMark();
                    numMark.setHabit(habit);
                    numMark.setDateEntity(dateEntity);
                    numMark.setValue(value);
                    numMarkRepo.save(numMark);
                }
                break;
            case bollean:
                booleanMarkRepo.deleteByHabitAndDateEntity(habit, dateEntity);
                if ((Boolean) markValue) {
                    BooleanMark booleanMark = new BooleanMark();
                    booleanMark.setHabit(habit);
                    booleanMark.setDateEntity(dateEntity);
                    booleanMarkRepo.save(booleanMark);
                }
                break;
            case enumeration:
                enumMarkRepo.deleteByHabitAndDateEntity(habit, dateEntity);
                if (markValue != null && !isEmptyEnumElement((EnumElement) markValue)) {
                    EnumMark enumMark = new EnumMark();
                    enumMark.setHabit(habit);
                    enumMark.setDateEntity(dateEntity);
                    enumMark.setEnumElement((EnumElement) markValue);
                    enumMarkRepo.save(enumMark);
                }
        }
    }

    private boolean isEmptyEnumElement(EnumElement enumElement) {
        return enumElement.getId() == -1L;
    }

    public HabitMark getLastMarkByHabitWithinLastWeekOrNull(Habit habit) {
        List<Date> dates = DateUtils.getDateRange(DateUtils.getLastWeekPeriod());
        dates.sort(Comparator.comparing(Date::getTime).reversed());
        for (Date date : dates) {
            HabitMark habitMark = getMarkOrNull(habit, date);
            if (habitMark != null)
                return habitMark;
        }
        return null;
    }

    // Без модификатора public екзешник не работает
    public HabitMark getMarkOrNull(Habit habit, Date date) {
        DateEntity dateEntity = dateService.getDateEntityIfExistsOrNull(date);

        if (dateEntity != null) {
            switch (habit.getType()) {
                case number:
                    return numMarkRepo.findByHabitAndDateEntity(habit, dateEntity).orElse(null);
                case bollean:
                    return booleanMarkRepo.findByHabitAndDateEntity(habit, dateEntity).orElse(null);
                case enumeration:
                    return enumMarkRepo.findByHabitAndDateEntity(habit, dateEntity).orElse(null);
            }
        }
        return null;
    }

    List<HabitMark> getMarkListByPeriod(Habit habit, Period period) {
        List<Date> dates = DateUtils.getDateRange(period);
        List<HabitMark> marks = new ArrayList<>();
        for (Date date : dates) {
            HabitMark mark = getMarkOrNull(habit, date);
            if (mark != null)
                marks.add(mark);
        }
        return marks;
    }

    public interface HabitMark {
        String getTextForReport();

        int getNumEquivalent();
    }
}
