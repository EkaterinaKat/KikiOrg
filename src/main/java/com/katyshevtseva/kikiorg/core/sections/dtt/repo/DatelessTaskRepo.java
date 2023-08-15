package com.katyshevtseva.kikiorg.core.sections.dtt.repo;

import com.katyshevtseva.kikiorg.core.sections.dtt.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DatelessTaskRepo extends JpaRepository<DatelessTask, Long>, JpaSpecificationExecutor<DatelessTask> {

    Long countByStatus(TaskStatus status);

    List<DatelessTask> getTop2ByStatusAndSphereOrderByCreationDateAsc(TaskStatus status, Sphere sphere);

    List<DatelessTask> findBySphere(Sphere sphere);
}
