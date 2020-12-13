package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumMark;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnumMarkRepo extends JpaRepository<EnumMark, Long> {
    void deleteByHabitAndDateEntity(Habit habit, DateEntity dateEntity);

    Optional<EnumMark> findByHabitAndDateEntity(Habit habit, DateEntity dateEntity);
}
