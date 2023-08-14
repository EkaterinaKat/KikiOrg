package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.DatelessTaskRepo;
import com.katyshevtseva.kikiorg.core.sections.dtt.repo.SphereRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DttTaskService {
    private static final int TASK_PAGE_SIZE = 15;
    private final DateService dateService;
    private final DatelessTaskRepo repo;
    private final SphereRepo sphereRepo;
    private final FakeService fakeService;

    public void createTask(Sphere sphere, String title) {
        fakeService.antiFakeCheck(sphere);
        DatelessTask task = new DatelessTask();
        task.setSphere(sphere);
        task.setTitle(title);
        task.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        repo.save(task);
    }

    public void edit(DatelessTask task, String title) {
        fakeService.antiFakeCheck(task);
        task.setTitle(title);
        repo.save(task);
    }

    public void delete(DatelessTask task) {
        fakeService.antiFakeCheck(task);
        repo.delete(task);
    }

    public void done(DatelessTask task) {
        fakeService.antiFakeCheck(task);
        task.setCompletionDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        repo.save(task);
    }

    public Page<DatelessTask> getTodoTasks(Sphere sphere, int pageNum) {
        if (sphere.getFake()) {
            return fakeService.getFakeTodoTasks(sphere, pageNum, TASK_PAGE_SIZE);
        }
        PageRequest pageRequest = PageRequest.of(pageNum, TASK_PAGE_SIZE, Sort.by("creationDate.value").ascending());

        org.springframework.data.domain.Page<DatelessTask> actionPage =
                repo.findBySphereAndCompletionDateIsNull(sphere, pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }

    public Page<DatelessTask> getDoneTasks(Sphere sphere, int pageNum) {
        if (sphere.getFake()) {
            return fakeService.getFakeDoneTasks(sphere);
        }
        PageRequest pageRequest = PageRequest.of(pageNum, TASK_PAGE_SIZE, Sort.by("id").ascending());

        org.springframework.data.domain.Page<DatelessTask> actionPage =
                repo.findBySphereAndCompletionDateIsNotNull(sphere, pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }

    public List<DatelessTask> getOldestTasks() {
        List<DatelessTask> result = new ArrayList<>();
        for (Sphere sphere : sphereRepo.findByActiveIsTrue()) {
            result.addAll(repo.getTop2ByCompletionDateIsNullAndSphereOrderByCreationDateAsc(sphere));
        }
        result.addAll(fakeService.getOldestTasks());
        return result;
    }

    public String getStatistics() {
        long todoCount = repo.countByCompletionDateIsNull();
        long done = repo.countByCompletionDateIsNotNull();
        return String.format("Todo: %d\nDone: %d", todoCount, done);
    }

    public String getStatistics(Sphere sphere) {
        if (sphere.getFake()) {
            return "";
        }
        long todoCount = repo.countBySphereAndCompletionDateIsNull(sphere);
        long done = repo.countBySphereAndCompletionDateIsNotNull(sphere);
        return String.format(" (%d/%d)", todoCount, done);
    }
}
