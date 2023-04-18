package com.katyshevtseva.kikiorg.core.sections.dtt.repo;

import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DttLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DttLogRepo extends JpaRepository<DttLog, Long> {
}
