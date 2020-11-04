package com.katyshevtseva.kikiorg.core.modes.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.HabitMark;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.ReportCell;
import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/* Если habitType == enum то сохраняется id EnumElement */
@Service
class HabitMarkConverter {
    private static final String GREEN = "#40FF23";
    private static final String WHITE = "#FFFFFF";
    @Autowired
    private EnumElementRepo enumElementRepo;

    static Long prepareToPersist(Habit habit, Object mark) {
        if (mark == null)
            throw new RuntimeException("Mark can not be null");
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
        Long markNum = mark.getMark();
        switch (habit.getType()) {
            case bollean:
                return ReportCell.filled(markNum.toString(), markNum == 1 ? GREEN : WHITE);
            case number:
                return ReportCell.filled(markNum.toString(), markNum == 0 ? WHITE : GREEN);
            case enumeration:
                EnumElement enumElement = enumElementRepo.findById(markNum).get();
                return ReportCell.filled(enumElement.getTitle(), GREEN);

        }
        return null;
    }
}
