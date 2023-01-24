package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepo extends JpaRepository<Goal, Long> {

    Page<Goal> findByActivityAndCompletionDateIsNull(Activity activity, Pageable pageable);

    Page<Goal> findByActivityAndCompletionDateIsNotNull(Activity activity, Pageable pageable);

    List<Goal> findByHighlightedIsTrue();

    int countByCompletionDateIsNull();

    int countByCompletionDateIsNotNull();

    int countByHighlightedIsTrue();
}
