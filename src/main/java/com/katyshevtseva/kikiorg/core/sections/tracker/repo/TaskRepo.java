package com.katyshevtseva.kikiorg.core.sections.tracker.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Long countByStatus(TaskStatus status);

    List<Task> getTop2ByStatusAndSphereOrderByCreationDateAsc(TaskStatus status, Sphere sphere);

    List<Task> findBySphere(Sphere sphere);
}
