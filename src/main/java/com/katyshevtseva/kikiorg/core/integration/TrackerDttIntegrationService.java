package com.katyshevtseva.kikiorg.core.integration;

import com.katyshevtseva.kikiorg.core.sections.dtt.DttTaskService;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.SphereRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackerDttIntegrationService {
    private final DttTaskService dttTaskService;
    private final SphereRepo sphereRepo;

    public void reportTrackerTaskCreation(Task task) {
        dttTaskService.createTask(sphereRepo.findById(11L).get(), task.getNumberAndTitleInfo());
    }
}
