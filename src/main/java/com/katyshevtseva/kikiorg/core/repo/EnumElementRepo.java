package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.modes.habits.EnumElement;
import com.katyshevtseva.kikiorg.core.modes.habits.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnumElementRepo extends JpaRepository<EnumElement, Long> {
    List<EnumElement> findByHabitId(long habitId);
}
