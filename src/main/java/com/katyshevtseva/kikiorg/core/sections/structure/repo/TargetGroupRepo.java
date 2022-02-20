package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TargetGroupRepo extends JpaRepository<TargetGroup, Long> {

    List<TargetGroup> findAllByParent(TargetGroup parent);
}
