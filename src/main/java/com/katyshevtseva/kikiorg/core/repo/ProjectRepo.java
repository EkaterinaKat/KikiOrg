package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
}
