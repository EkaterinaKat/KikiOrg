package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseChangeActionRepo extends JpaRepository<CourseChangeAction, Long> {
}
