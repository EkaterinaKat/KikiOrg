package com.katyshevtseva.kikiorg.core.modes.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;


/* Если habitType == enum то сохраняется и возвращается id EnumElement */
class HabitMarkConverter {

    static Long convertToString(Habit habit, Object mark) {
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

    static Object convertToMark(Habit habit, Long mark){
        switch (habit.getType()) {
            case enumeration:
            case number:
                return mark;
            case bollean:
                if (mark==1)
                    return true;
                if (mark==0)
                    return false;
        }
        throw new RuntimeException();
    }
}
