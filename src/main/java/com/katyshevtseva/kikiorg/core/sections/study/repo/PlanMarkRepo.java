package com.katyshevtseva.kikiorg.core.sections.study.repo;

import com.katyshevtseva.kikiorg.core.sections.study.entity.PlanMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface PlanMarkRepo extends JpaRepository<PlanMark, Long> {

    boolean existsByDateEntityValueAndSubject(Date date, Subject subject);

    Optional<PlanMark> findByDateEntityValueAndSubject(Date date, Subject subject);
}
