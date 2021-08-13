package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentEntityRepo extends JpaRepository<ComponentEntity, Long> {
}
