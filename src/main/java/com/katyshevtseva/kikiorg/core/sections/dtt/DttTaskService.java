package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.DatelessTaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class DttTaskService {
    private final DateService dateService;
    private final DatelessTaskRepo repo;

    public void createTask(Sphere sphere, String title) {
        DatelessTask task = new DatelessTask();
        task.setSphere(sphere);
        task.setTitle(title);
        task.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        repo.save(task);
    }

    public void edit(DatelessTask task, String title) {
        task.setTitle(title);
        repo.save(task);
    }

    public void delete(DatelessTask task) {
        repo.delete(task);
    }

    public void done(DatelessTask task) {
        task.setCompletionDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        repo.save(task);
    }

    public Page<DatelessTask> getTodoTasks(Sphere sphere, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("id").ascending());

        org.springframework.data.domain.Page<DatelessTask> actionPage =
                repo.findBySphereAndCompletionDateIsNull(sphere, pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }

    public Page<DatelessTask> getDoneTasks(Sphere sphere, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("id").ascending());

        org.springframework.data.domain.Page<DatelessTask> actionPage =
                repo.findBySphereAndCompletionDateIsNotNull(sphere, pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }
}
