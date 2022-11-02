package com.katyshevtseva.kikiorg.core.sections.tracker.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepo extends JpaRepository<ColorEntity, Long> {
    Optional<ColorEntity> findFirstByRedAndGreenAndBlue(double red, double green, double blue);
}
