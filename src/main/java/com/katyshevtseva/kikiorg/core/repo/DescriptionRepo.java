package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Description;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DescriptionRepo extends JpaRepository<Description, Long> {

    List<Description> findByHabit(Habit habit);

    default Description getOldestDesc(Habit habit) {
        return findByHabitOrderByBeginningDate(habit, PageRequest.of(0, 1)).get(0);
    }

    @Query("SELECT d FROM Description d JOIN d.beginningDate bd WHERE d.habit = :habit ORDER BY bd.value")
    List<Description> findByHabitOrderByBeginningDate(@Param("habit") Habit habit, Pageable pageable);

    Optional<Description> findByHabitAndBeginningDate(Habit habit, DateEntity beginningDate);
}
