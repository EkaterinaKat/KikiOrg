package com.katyshevtseva.kikiorg.core.sections.dtt.repo;

import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatelessTaskRepo extends JpaRepository<DatelessTask, Long> {

    Page<DatelessTask> findBySphereAndCompletionDateIsNull(Sphere sphere, Pageable pageable);

    Page<DatelessTask> findBySphereAndCompletionDateIsNotNull(Sphere sphere, Pageable pageable);
}
