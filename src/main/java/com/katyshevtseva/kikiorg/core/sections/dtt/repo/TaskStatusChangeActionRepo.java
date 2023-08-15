package com.katyshevtseva.kikiorg.core.sections.dtt.repo;

import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.TaskStatusChangeAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStatusChangeActionRepo extends JpaRepository<TaskStatusChangeAction, Long> {

    List<TaskStatusChangeAction> findAllByTask(DatelessTask task);
}
