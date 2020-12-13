package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.BooleanMark;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BooleanMarkRepo extends JpaRepository<BooleanMark, Long> {
    void deleteByHabitAndDateEntity(Habit habit, DateEntity dateEntity);

    Optional<BooleanMark> findByHabitAndDateEntity(Habit habit, DateEntity dateEntity);
}
