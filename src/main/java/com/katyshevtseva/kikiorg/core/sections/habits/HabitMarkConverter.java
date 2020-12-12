package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/* Если habitType == enum то сохраняется id EnumElement */
/* Отметка не может быть null */
@Service
class HabitMarkConverter {
    private static final String GREEN = "#40FF23";
    private static final String WHITE = "#FFFFFF";
    @Autowired
    private EnumElementRepo enumElementRepo;

    boolean needToPersist(Habit habit, Object mark) {
        if (mark == null)
            return false;
        return (habit.getType() != HabitType.number && habit.getType() != HabitType.bollean)
                || prepareToPersist(habit, mark) != 0;
    }

    Long prepareToPersist(Habit habit, Object mark) {
        switch (habit.getType()) {
            case enumeration:
                return ((EnumElement) mark).getId();
            case number:
                return (Long) mark;
            case bollean:
                return ((boolean) mark) ? 1L : 0L;
        }
        throw new RuntimeException();
    }

    ReportCell prepareForReport(Habit habit, HabitMark mark) {
        Long numRepresentation = mark.getNumRepresentation();
        switch (habit.getType()) {
            case bollean:
                return ReportCell.filled("", numRepresentation == 1 ?
                        ReportCell.Color.GREEN : ReportCell.Color.WHITE);
            case number:
                return ReportCell.filled(numRepresentation.toString(), numRepresentation == 0 ?
                        ReportCell.Color.WHITE : ReportCell.Color.GREEN);
            case enumeration:
                EnumElement enumElement = enumElementRepo.findById(numRepresentation).get();
                return ReportCell.filled(enumElement.getTitle(), ReportCell.Color.GREEN);
        }
        return null;
    }
}
