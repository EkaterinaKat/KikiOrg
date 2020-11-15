package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabitMarkRepo extends JpaRepository<HabitMark, Long> {
    Optional<HabitMark> findByHabitIdAndDateEntity(long habitId, DateEntity dateEntity);
}
