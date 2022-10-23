package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.HabitChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.repo.MarkRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitChangeAction;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitTest implements TestClass {
    private final HabitRepo habitRepo;
    private final HabitChangeActionRepo habitChangeActionRepo;
    private final MarkRepo markRepo;

    public boolean test() {
        boolean success = true;

        for (Habit habit : habitRepo.findAll()) {

            // Должно быть хоть одно изменение у привычки
            List<HabitChangeAction> changeActions = habitChangeActionRepo.findAllByHabit(habit);
            if (changeActions.isEmpty()) {
                success = false;
                System.out.println(habit.getTitle() + " не имеет ни одного изменения");
                throw new RuntimeException(habit.getTitle() + " не имеет ни одного изменения");
            }

            // У привычки не должно быть отметок о выполнении раньше начала действия первого описания
            HabitChangeAction firstChangeAction = changeActions.stream()
                    .min(Comparator.comparing(habitChangeAction -> habitChangeAction.getDateEntity().getValue())).get();
            Date dateOfFirstChangeAction = firstChangeAction.getDateEntity().getValue();
            for (Mark mark : markRepo.getMarksOrderedByDate(habit, PageRequest.of(0, 1))) {
                if (DateUtils.before(mark.getDateEntity().getValue(), dateOfFirstChangeAction)) {
                    success = false;
                    System.out.println(habit.getTitle() + ": отметка о выполнении раньше начала действия первого описания");
                }
            }

            // Текст последнего описания должен совпадать с описанием привычки
            HabitChangeAction lastChangeAction = changeActions.stream()
                    .max(Comparator.comparing(habitChangeAction -> habitChangeAction.getDateEntity().getValue())).get();
            if (!lastChangeAction.getDescription().equals(habit.getDescription())) {
                success = false;
                System.out.println(habit.getTitle() + ": текст последнего описания не совпадает с описанием привычки");
            }

            // Не должно быть два изменения описания в один день
            Set<Date> datesSet = changeActions.stream()
                    .map(habitChangeAction -> habitChangeAction.getDateEntity().getValue())
                    .collect(Collectors.toSet());
            if (datesSet.size() != changeActions.size()) {
                success = false;
                System.out.println(habit.getTitle() + " имеет более одного описания на одну дату");
            }

            // Не должно существовать двух отметок для одной привычки и одной даты
            List<Mark> marks = markRepo.findByHabit(habit);
            Set<Date> marksDatesSet = marks.stream()
                    .map(mark -> mark.getDateEntity().getValue())
                    .collect(Collectors.toSet());
            if (marksDatesSet.size() != marks.size()) {
                success = false;
                System.out.println(habit.getTitle() + " имеет более одной отметки на одну дату");
            }
        }

        return success;
    }

    @Override
    public String getTitle() {
        return "habitTest";
    }
}
