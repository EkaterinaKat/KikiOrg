package com.katyshevtseva.kikiorg.core.sections.wardrobe.repo;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentEntityRepo extends JpaRepository<ComponentEntity, Long> {
    void deleteByCollageEntity(CollageEntity collageEntity);
}
