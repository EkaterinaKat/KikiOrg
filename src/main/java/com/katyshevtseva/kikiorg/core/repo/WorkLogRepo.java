package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.work.WorkArea;
import com.katyshevtseva.kikiorg.core.sections.work.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkLogRepo extends JpaRepository<WorkLog, Long> {
    List<WorkLog> findByDateEntityAndWorkArea(DateEntity dateEntity, WorkArea workArea);
}
