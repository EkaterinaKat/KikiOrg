package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.integration.TrackerDttIntegrationService;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.ProjectRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TrackerService {
    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;
    private final DateService dateService;
    private final TrackerDttIntegrationService integrationService;

    public void saveProject(Project project) {
        projectRepo.save(project);
    }

    public void saveEditedTask(Task task) {
        taskRepo.save(task);
    }

    public void createTask(String title, String description, Project project) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setProject(project);
        task.setNumber(getProjectNextNumber(project));
        task.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        task.setTaskStatus(TaskStatus.TODO);
        integrationService.reportTrackerTaskCreation(taskRepo.save(task));
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public List<Project> getProjectsWithTasksInStatus(TaskStatus taskStatus) {
        return projectRepo.findAll().stream()
                .filter(project -> taskRepo.countByProjectAndTaskStatus(project, taskStatus) > 0)
                .collect(Collectors.toList());
    }

    public void completeTask(Task task) {
        task.setCompletionDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        task.setTaskStatus(TaskStatus.DONE);
        taskRepo.save(task);
    }

    public void rejectTask(Task task) {
        task.setCompletionDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        task.setTaskStatus(TaskStatus.REJECTED);
        taskRepo.save(task);
    }

    public void shelveTask(Task task) {
        task.setTaskStatus(TaskStatus.SHELVED);
        taskRepo.save(task);
    }

    public void returnTaskToWork(Task task) {
        task.setCompletionDate(null);
        task.setTaskStatus(TaskStatus.TODO);
        taskRepo.save(task);
    }

    public String getStatistics() {
        return String.format("To Do: %d. \nShelved: %d. \nDone: %d. \nRejected: %d.",
                taskRepo.countByTaskStatus(TaskStatus.TODO),
                taskRepo.countByTaskStatus(TaskStatus.SHELVED),
                taskRepo.countByTaskStatus(TaskStatus.DONE),
                taskRepo.countByTaskStatus(TaskStatus.REJECTED));
    }

    private int getProjectNextNumber(Project project) {
        Optional<Task> optionalTask = taskRepo.findFirstByProjectOrderByNumberDesc(project);
        return optionalTask.map(task -> task.getNumber() + 1).orElse(1);
    }
}
