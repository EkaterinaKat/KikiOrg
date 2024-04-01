package com.katyshevtseva.kikiorg.core.sections.study.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SubjMarkRepo extends JpaRepository<SubjMark, Long> {

    List<SubjMark> findBySubjectAndDateEntity(Subject subject, DateEntity dateEntity);

    List<SubjMark> findByDateEntityValue(Date date);

    List<SubjMark> findBySubject(Subject subject);

    List<SubjMark> findBySubjectOrderByDateEntityValue(Subject subject);

    void deleteBySubject(Subject subject);

    SubjMark findFirstBySubjectOrderByDateEntityValue(Subject subject);

    boolean existsBySubjectAndDateEntityValue(Subject subject, Date date);

    List<SubjMark> findBySubjectAndDateEntityValue(Subject subject, Date date);
}
