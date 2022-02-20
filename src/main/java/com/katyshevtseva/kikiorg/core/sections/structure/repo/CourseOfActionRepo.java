package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseOfActionRepo extends JpaRepository<CourseOfAction, Long> {

    List<CourseOfAction> findByStatus(CourseOfActionStatus status);
}
