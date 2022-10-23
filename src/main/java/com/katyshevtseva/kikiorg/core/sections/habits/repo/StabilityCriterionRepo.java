package com.katyshevtseva.kikiorg.core.sections.habits.repo;

import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StabilityCriterionRepo extends JpaRepository<StabilityCriterion, Long> {
    @Modifying
    @Transactional
    void deleteByHabit(Habit habit);

    Optional<StabilityCriterion> findByHabit(Habit habit);
}
