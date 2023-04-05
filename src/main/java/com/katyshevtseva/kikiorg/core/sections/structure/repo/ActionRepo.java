package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Action;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepo extends JpaRepository<Action, Long> {

    Page<Action> findByActivityAndCompletionDateIsNull(Activity activity, Pageable pageable);

    Page<Action> findByActivityAndCompletionDateIsNotNull(Activity activity, Pageable pageable);

    int countByCompletionDateIsNull();

    int countByCompletionDateIsNotNull();
}
