package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StabilityCriterionRepo extends JpaRepository<StabilityCriterion, Long> {
    void deleteByHabit(Habit habit);

    Optional<StabilityCriterion> findByHabit(Habit habit);
}
