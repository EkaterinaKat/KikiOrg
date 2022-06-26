package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepo extends JpaRepository<ColorEntity, Long> {
    Optional<ColorEntity> findFirstByRedAndGreenAndBlue(double red, double green, double blue);
}
