package com.katyshevtseva.kikiorg.core.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    Optional<Task> findFirstByProjectOrderByNumberDesc(Project project);

    int countByTaskStatus(TaskStatus taskStatus);

    Page<Task> findByTaskStatus(TaskStatus taskStatus, Pageable pageable);

    Page<Task> findByTaskStatusAndProject(TaskStatus taskStatus, Project project, Pageable pageable);

    Long countByProjectAndTaskStatus(Project project, TaskStatus taskStatus);
}
