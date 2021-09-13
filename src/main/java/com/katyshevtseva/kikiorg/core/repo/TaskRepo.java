package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    Optional<Task> findFirstByProjectOrderByNumberDesc(Project project);

    int countByTaskStatus(TaskStatus taskStatus);

    Page<Task> findByTaskStatus(TaskStatus taskStatus, Pageable pageable);
}
