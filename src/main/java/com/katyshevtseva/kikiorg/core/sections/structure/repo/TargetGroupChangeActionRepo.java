package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroupChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetGroupChangeActionRepo extends JpaRepository<TargetGroupChangeAction, Long> {

    void deleteByTargetGroup(TargetGroup targetGroup);
}
