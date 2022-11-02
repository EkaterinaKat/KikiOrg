package com.katyshevtseva.kikiorg.core.sections.tracker.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Project, Long> {
}
