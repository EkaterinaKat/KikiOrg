package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.repo.MarkRepo;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.equalsIgnoreTime;
import static com.katyshevtseva.date.DateUtils.shiftDate;
import static java.util.Comparator.comparing;

@Service
@Transactional
@RequiredArgsConstructor
public class UninterruptedPeriodService {
    private final MarkRepo markRepo;

    public List<Habit> orderByLengthOfCurrentUp(List<Habit> habits) {
        return habits.stream()
                .sorted(comparing(this::getLengthOfCurrentUp).reversed())
                .collect(Collectors.toList());
    }

    public int getLengthOfCurrentUp(Habit habit) {
        Period periodEndedTodayOrYesterday = getPeriodEndedTodayOrYesterdayOrNull(habit);

        if (periodEndedTodayOrYesterday == null) {
            return 0;
        }

        return DateUtils.getNumberOfDays(periodEndedTodayOrYesterday) + 1;
    }

    private Period getPeriodEndedTodayOrYesterdayOrNull(Habit habit) {
        List<Period> periodsEndedTodayOrYesterday = getAllUps(habit).stream()
                .filter(period -> (equalsIgnoreTime(period.end(), new Date())
                        || equalsIgnoreTime(period.end(), shiftDate(new Date(), DateUtils.TimeUnit.DAY, -1))))
                .collect(Collectors.toList());

        if (periodsEndedTodayOrYesterday.size() > 1) {
            throw new RuntimeException();
        }

        if (periodsEndedTodayOrYesterday.isEmpty()) {
            return null;
        }

        return periodsEndedTodayOrYesterday.get(0);
    }

    private List<Period> getAllUps(Habit habit) {
        List<Period> periods = new ArrayList<>();
        List<Mark> marks = markRepo.getMarksOrderedByDate(habit);

        if (marks.isEmpty()) {
            return periods;
        }

        Date start = null;
        Date end = null;
        for (Mark mark : marks) {
            if (start == null && end == null) {
                start = mark.getDateEntity().getValue();
            } else {
                if (!mark.getDateEntity().getValue().equals(shiftDate(end, DateUtils.TimeUnit.DAY, 1))) {
                    periods.add(new Period(start, end));
                    start = mark.getDateEntity().getValue();
                }
            }
            end = mark.getDateEntity().getValue();
        }
        periods.add(new Period(start, end));

        return periods;
    }
}
