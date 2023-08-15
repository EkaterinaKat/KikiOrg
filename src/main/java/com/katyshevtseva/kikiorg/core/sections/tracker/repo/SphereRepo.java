package com.katyshevtseva.kikiorg.core.sections.tracker.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Sphere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SphereRepo extends JpaRepository<Sphere, Long> {
    List<Sphere> findByActiveIsTrue();
}
