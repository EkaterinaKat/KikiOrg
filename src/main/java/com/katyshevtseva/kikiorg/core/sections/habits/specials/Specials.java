package com.katyshevtseva.kikiorg.core.sections.habits.specials;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.repo.EnumElementRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitsRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.NumMark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Specials {
    @Autowired
    private HabitMarkService markService;
    @Autowired
    private HabitsRepo habitsRepo;
    @Autowired
    private EnumElementRepo enumElementRepo;

    //    @PostConstruct
    public void severalHabitsToOne() {
        System.out.println("start");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Habit habit1 = habitsRepo.findById(7L).get();
        Habit habit2 = habitsRepo.findById(8L).get();
        Habit habit3 = habitsRepo.findById(9L).get();
        Habit habit20 = habitsRepo.findById(21L).get();

        EnumElement enumElement1 = enumElementRepo.findById(16L).get();
        EnumElement enumElement2 = enumElementRepo.findById(17L).get();

        List<Date> dates = null;
        try {
            dates = DateUtils.getDateRange(
                    new Period(dateFormat.parse("01.03.2020"), dateFormat.parse("03.03.2021")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Date date : dates) {
            NumMark mark1 = (NumMark) markService.getMarkOrNull(habit1, date);
            NumMark mark2 = (NumMark) markService.getMarkOrNull(habit2, date);
            NumMark mark3 = (NumMark) markService.getMarkOrNull(habit3, date);

            if (mark1 != null && mark2 != null && mark3 != null) {
                if (mark1.getValue() > 19 && mark2.getValue() > 19 && mark3.getValue() > 19)
                    markService.saveMarkOrRewriteIfExists(habit20, date, enumElement1);

                if (mark1.getValue() == 30 && mark2.getValue() == 30 && mark3.getValue() == 30)
                    markService.saveMarkOrRewriteIfExists(habit20, date, enumElement2);
            }
        }
        System.out.println("done");
    }
}
