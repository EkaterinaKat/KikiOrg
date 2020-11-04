package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.habits.entity.HabitMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabitMarkRepo extends JpaRepository<HabitMark, Long> {
    Optional<HabitMark> findByHabitIdAndDateEntityId(long habitId, long dateEntityId);
}
