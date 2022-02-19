package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetGroupRepo extends JpaRepository<TargetGroup, Long> {
}
