package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseOfActionRepo extends JpaRepository<CourseOfAction, Long> {

    Page<CourseOfAction> findByStatus(CourseOfActionStatus status, Pageable pageable);

    Optional<CourseOfAction> findByRootTargetGroup(TargetGroup targetGroup);
}
