package com.katyshevtseva.kikiorg.core.sections.tracker.repo;

import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.TaskStatusChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStatusChangeActionRepo extends JpaRepository<TaskStatusChangeAction, Long> {

    List<TaskStatusChangeAction> findAllByTask(Task task);
}
