package com.katyshevtseva.kikiorg.core.sections.study.repo;

import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepo extends JpaRepository<Subject, Long> {

    List<Subject> findAllByOrderByTitle();

    List<Subject> findAllByArchivedFalseOrderByTitle();

    List<Subject> findAllByArchivedFalseAndHiddenFalseOrderByTitle();

    List<Subject> findAllByArchivedFalseAndHiddenTrueOrderByTitle();
}
