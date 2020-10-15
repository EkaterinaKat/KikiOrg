package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.habits.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitsRepo extends JpaRepository<Habit, Long> {
}
