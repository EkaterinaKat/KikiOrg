package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MarkRepo extends JpaRepository<Mark, Long> {
    void deleteByHabitAndDateEntity(Habit habit, DateEntity dateEntity);

    Optional<Mark> findByHabitAndDateEntity(Habit habit, DateEntity dateEntity);

    @Query("SELECT m FROM Mark m JOIN m.dateEntity d " +
            "WHERE m.habit = :habit ORDER BY d.value ASC ")
    Page<Mark> getOrderedByDateMarks(@Param("habit") Habit habit, Pageable pageable);
}
