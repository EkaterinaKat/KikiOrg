package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitChangeActionRepo extends JpaRepository<HabitChangeAction, Long> {
}
