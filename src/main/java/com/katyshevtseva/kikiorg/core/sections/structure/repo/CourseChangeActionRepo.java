package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseChangeAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseChangeActionRepo extends JpaRepository<CourseChangeAction, Long> {

    void deleteByCourseOfAction(CourseOfAction courseOfAction);
}
