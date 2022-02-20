package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TargetRepo extends JpaRepository<Target, Long> {

    List<Target> findByParent(TargetGroup parent);
}
