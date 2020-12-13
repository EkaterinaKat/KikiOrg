package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.NumMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NumMarkRepo extends JpaRepository<NumMark, Long> {
    void deleteByHabitAndDateEntity(Habit habit, DateEntity dateEntity);

    Optional<NumMark> findByHabitAndDateEntity(Habit habit, DateEntity dateEntity);
}
