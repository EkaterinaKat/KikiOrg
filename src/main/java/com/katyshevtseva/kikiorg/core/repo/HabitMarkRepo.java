package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitMark;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitMarkRepo extends JpaRepository<HabitMark, Long> {
    Optional<HabitMark> findByHabitIdAndDateEntity(long habitId, DateEntity dateEntity);

    default HabitMark getLatestHabitMarkIfExistsOrNull(Habit habit) {
        List<HabitMark> habitMarks = findByHabit(habit, PageRequest.of(0, 1));
        if (habitMarks.size() > 0)
            return habitMarks.get(0);
        return null;
    }

    List<HabitMark> findByHabit(Habit habit, Pageable pageable);
}
