package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.DescriptionRepo;
import com.katyshevtseva.kikiorg.core.repo.HabitRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Description;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.kikiorg.core.tests.TestConstants.ERROR_STRING;
import static com.katyshevtseva.kikiorg.core.tests.TestConstants.FAILED_STRING;

@Service
@RequiredArgsConstructor
public class HabitDescTest {
    private final HabitRepo habitRepo;
    private final DescriptionRepo descriptionRepo;
    private final DateService dateService;
    private final boolean BREAK_AFTER_ERROR = true;

    public void test() {
        boolean succes = true;
        for (Habit habit : habitRepo.findAll()) {
            System.out.println("***********************************************\n");
            System.out.println(habit.getTitle().toUpperCase());
            if (!checkPresence(habit)) {
                succes = false;
                if (BREAK_AFTER_ERROR)
                    break;
            }
            if (!checkDatesOfCurrentDesc(habit)) {
                succes = false;
                if (BREAK_AFTER_ERROR)
                    break;
            }
            if (!checkOtherDescDates(habit)) {
                succes = false;
                if (BREAK_AFTER_ERROR)
                    break;
            }
            if (!checkIfPeriodOfDescIsMoreThanOneDay(habit)) {
                succes = false;
                if (BREAK_AFTER_ERROR)
                    break;
            }
            if (!checkConsistencyOfDescPeriods(habit)) {
                succes = false;
                if (BREAK_AFTER_ERROR)
                    break;
            }
        }
        if (succes)
            System.out.println(String.format(TestConstants.BIG_SUCCESS_BANNER, "HABIT"));
        else
            System.out.println(FAILED_STRING);
    }

    private boolean checkConsistencyOfDescPeriods(Habit habit) {
        boolean success = true;
        System.out.println("Habit history consistency check");
        Description description = descriptionRepo.getOldestDesc(habit);
        while (!description.equals(habit.getCurrentDescription())) {
            System.out.println(String.format("  desc_id = %d. %s - %s", description.getId(),
                    READABLE_DATE_FORMAT.format(description.getBeginningDate().getValue()),
                    READABLE_DATE_FORMAT.format(description.getEndDate().getValue())));
            Optional<Description> optionalDescription = descriptionRepo.findByHabitAndBeginningDate(
                    habit, dateService.createIfNotExistAndGetDateEntity(
                            shiftDate(description.getEndDate().getValue(), TimeUnit.DAY, 1)));
            if (!optionalDescription.isPresent()) {
                success = false;
                break;
            }
            description = optionalDescription.get();
        }
        System.out.println(String.format("  desc_id = %d. %s - present time",
                description.getId(), READABLE_DATE_FORMAT.format(description.getBeginningDate().getValue())));
        if (success)
            System.out.println("Habit history is consistent +");
        else
            System.out.println(ERROR_STRING + " problems with habit history consistency");
        return success;
    }

    private boolean checkIfPeriodOfDescIsMoreThanOneDay(Habit habit) {
        boolean success = true;
        for (Description description : descriptionRepo.findByHabit(habit)) {
            if (!description.equals(habit.getCurrentDescription())) {
                List<Date> dates = getDateRange(new Period(description.getBeginningDate().getValue(), description.getEndDate().getValue()));
                if (dates.size() < 1) {
                    success = false;
                    System.out.println(ERROR_STRING + " something wrong with period of decs with id = " + description.getId());
                }
            }
        }
        if (success)
            System.out.println("all ok with descriptions periods +");
        return success;
    }

    private boolean checkPresence(Habit habit) {
        if (habit.getCurrentDescription() != null) {
            System.out.println("has desc +");
            return true;
        } else {
            System.out.println(ERROR_STRING + " doesn't have desc");
            return false;
        }
    }

    private boolean checkDatesOfCurrentDesc(Habit habit) {
        if (habit.getCurrentDescription().getBeginningDate() != null && habit.getCurrentDescription().getEndDate() == null) {
            System.out.println("current desc has start date and doesn't have end date +");
            return true;
        } else {
            System.out.println(ERROR_STRING + " problem with current desc dates");
            return false;
        }
    }

    private boolean checkOtherDescDates(Habit habit) {
        boolean success = true;
        for (Description description : descriptionRepo.findByHabit(habit)) {
            if (!description.equals(habit.getCurrentDescription())
                    && (description.getBeginningDate() == null || description.getEndDate() == null)) {
                System.out.println(ERROR_STRING + " problem with dates of desc with id = " + description.getId());
                success = false;
            }
        }
        if (success)
            System.out.println("all other descriptions has both dates +");
        return success;
    }
}
