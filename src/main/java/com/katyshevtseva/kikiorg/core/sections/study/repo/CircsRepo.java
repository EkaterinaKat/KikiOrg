package com.katyshevtseva.kikiorg.core.sections.study.repo;

import com.katyshevtseva.kikiorg.core.sections.study.entity.Circs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CircsRepo extends JpaRepository<Circs, Long> {

    boolean existsByDateEntityValue(Date date);

    void deleteByDateEntityValue(Date date);

    List<Circs> findByDateEntityValue(Date date);
}
