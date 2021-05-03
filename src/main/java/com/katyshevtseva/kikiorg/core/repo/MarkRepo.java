package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkRepo extends JpaRepository<Mark, Long> {
    void deleteByHabitAndDateEntity(Habit habit, DateEntity dateEntity);

    Optional<Mark> findByHabitAndDateEntity(Habit habit, DateEntity dateEntity);
}
