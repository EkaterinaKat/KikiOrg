package com.katyshevtseva.kikiorg.core.sections.study.repo;

import com.katyshevtseva.kikiorg.core.sections.study.entity.Plan;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepo extends JpaRepository<Plan, Long> {

    List<Plan> findAllByArchivedFalseOrderByIdDesc();

    List<Plan> findAllByOrderByArchivedAscIdDesc();

    void deleteBySubject(Subject subject);
}
