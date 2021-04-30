package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepo extends JpaRepository<Habit, Long> {
    List<Habit> findByActiveTrue();
}
