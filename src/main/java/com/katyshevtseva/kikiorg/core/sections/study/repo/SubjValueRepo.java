package com.katyshevtseva.kikiorg.core.sections.study.repo;

import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjValue;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjValueRepo extends JpaRepository<SubjValue, Long> {

    List<SubjValue> findAllBySubject(Subject subject);

    void deleteBySubject(Subject subject);
}
