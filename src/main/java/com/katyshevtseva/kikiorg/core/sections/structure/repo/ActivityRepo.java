package com.katyshevtseva.kikiorg.core.sections.structure.repo;

import com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {

    List<Activity> findByStatus(ActivityStatus status);

    List<Activity> findByStatusAndGoal(ActivityStatus status, Goal goal);

    List<Activity> findByGoal(Goal goal);
}
