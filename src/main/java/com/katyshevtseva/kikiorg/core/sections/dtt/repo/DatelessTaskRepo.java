package com.katyshevtseva.kikiorg.core.sections.dtt.repo;

import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatelessTaskRepo extends JpaRepository<DatelessTask, Long> {

    Page<DatelessTask> findBySphereAndCompletionDateIsNull(Sphere sphere, Pageable pageable);

    Long countBySphereAndCompletionDateIsNull(Sphere sphere);

    Long countBySphereAndCompletionDateIsNotNull(Sphere sphere);

    Long countByCompletionDateIsNull();

    Long countByCompletionDateIsNotNull();

    Page<DatelessTask> findBySphereAndCompletionDateIsNotNull(Sphere sphere, Pageable pageable);

    List<DatelessTask> getTop2ByCompletionDateIsNullAndSphereOrderByCreationDateAsc(Sphere sphere);

    List<DatelessTask> findBySphere(Sphere sphere);
}
