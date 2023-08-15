package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.TaskStatusChangeAction;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.SphereRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.TaskRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.TaskStatusChangeActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;

@RequiredArgsConstructor
@Service
public class TaskService {
    private static final int TASK_PAGE_SIZE = 15;
    private final DateService dateService;
    private final TaskRepo repo;
    private final SphereRepo sphereRepo;
    private final TaskStatusChangeActionRepo actionRepo;

    public void createTask(Sphere sphere, String title, String desc) {
        Task task = new Task();
        task.setSphere(sphere);
        task.setTitle(title);
        task.setDescription(desc);
        task.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        task.setStatus(TaskStatus.TODO);
        repo.save(task);
    }

    public void edit(Task task, String title, String desc) {
        task.setTitle(title);
        task.setDescription(desc);
        repo.save(task);
    }

    public void delete(Task task) {
        for (TaskStatusChangeAction action : actionRepo.findAllByTask(task)) {
            actionRepo.delete(action);
        }
        repo.delete(repo.findById(task.getId()).get());
    }

    public void changeStatus(Task task, TaskStatus status) {
        if (!getStatusesProperToChangeTo(task.getStatus()).contains(status)) {
            throw new RuntimeException();
        }

        actionRepo.save(new TaskStatusChangeAction(
                task, dateService.createIfNotExistAndGetDateEntity(new Date()), status));
        task.setStatus(status);
        repo.save(task);
    }

    public List<TaskStatus> getStatusesProperToChangeTo(TaskStatus initStatus) {
        switch (initStatus) {
            case TODO:
                return Arrays.asList(TaskStatus.SHELVED, TaskStatus.DONE, TaskStatus.REJECTED);
            case SHELVED:
                return Arrays.asList(TaskStatus.TODO, TaskStatus.DONE, TaskStatus.REJECTED);
            case DONE:
            case REJECTED:
                return Collections.singletonList(TaskStatus.TODO);
        }
        throw new RuntimeException();
    }

    public Page<Task> getTasks(Sphere sphere, TaskStatus status, int pageNum, String searchString) {
        Pageable pageable = PageRequest.of(pageNum, TASK_PAGE_SIZE, Sort.by("id").descending());
        TaskSpec spec = new TaskSpec(sphere, status, searchString);
        org.springframework.data.domain.Page<Task> taskPage = repo.findAll(spec, pageable);
        return new Page<>(taskPage.getContent(), pageNum, taskPage.getTotalPages());
    }

    public List<Task> getOldestTasks() {
        List<Task> result = new ArrayList<>();
        for (Sphere sphere : sphereRepo.findByActiveIsTrue()) {
            result.addAll(repo.getTop2ByStatusAndSphereOrderByCreationDateAsc(TaskStatus.TODO, sphere));
        }
        return result;
    }

    public String getStatistics() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TaskStatus status : TaskStatus.values()) {
            stringBuilder.append(status.toString()).append(": ").append(repo.countByStatus(status)).append("\n");
        }
        return stringBuilder.toString();
    }

    public String getHistory(Task task) {
        StringBuilder stringBuilder = new StringBuilder("CREATED: ")
                .append(READABLE_DATE_FORMAT.format(task.getCreationDate().getValue()));
        for (TaskStatusChangeAction action : actionRepo.findAllByTask(task)) {
            stringBuilder.append("\n").append(action.toString());
        }
        return stringBuilder.toString();
    }

}
