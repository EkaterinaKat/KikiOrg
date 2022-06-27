package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface HabitChangeActionRepo extends JpaRepository<HabitChangeAction, Long> {

    void removeByHabitAndDateEntityValue(Habit habit, Date date);

    Optional<HabitChangeAction> findFirstByHabitOrderByDateEntityValueDesc(Habit habit);

    Optional<HabitChangeAction> findFirstByHabitOrderByDateEntityValueAsc(Habit habit);
}
