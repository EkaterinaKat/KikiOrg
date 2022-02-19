package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepo extends JpaRepository<Target, Long> {
}
