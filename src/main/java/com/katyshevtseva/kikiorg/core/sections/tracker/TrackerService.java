package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.repo.ProjectRepo;
import com.katyshevtseva.kikiorg.core.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TrackerService {
    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;
    private final DateService dateService;
    private final ColorService colorService;

    public void saveProject(Project project) {
        project.setColor(colorService.getSavedColorEntity(project.getColor()));
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
        saveEditedTask(task);
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
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

    public void returnTaskToWork(Task task) {
        task.setCompletionDate(null);
        task.setTaskStatus(TaskStatus.TODO);
        taskRepo.save(task);
    }

    public String getStatistics() {
        return String.format("To Do: %d. Done: %d. Rejected: %d.",
                taskRepo.countByTaskStatus(TaskStatus.TODO),
                taskRepo.countByTaskStatus(TaskStatus.DONE),
                taskRepo.countByTaskStatus(TaskStatus.REJECTED));
    }

    private int getProjectNextNumber(Project project) {
        Optional<Task> optionalTask = taskRepo.findFirstByProjectOrderByNumberDesc(project);
        return optionalTask.map(task -> task.getNumber() + 1).orElse(1);
    }
}
