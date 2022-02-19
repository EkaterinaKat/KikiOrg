package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetChangeActionRepo extends JpaRepository<TargetChangeAction, Long> {
}
