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

    public Task saveTask(Task task) {
        return taskRepo.save(task);
    }

    public Task createTask(String title, String description, Project project) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setProject(project);
        task.setNumber(getProjectNextNumber(project));
        task.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        task.setTaskStatus(TaskStatus.TODO);
        return saveTask(task);
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public List<Task> getTodoTasks() {
        return taskRepo.findAllByTaskStatus(TaskStatus.TODO);
    }

    public List<Task> getAllDoneAndRejectedTasks() {
        List<Task> tasks = taskRepo.findAllByTaskStatus(TaskStatus.REJECTED);
        tasks.addAll(taskRepo.findAllByTaskStatus(TaskStatus.DONE));
        return tasks;
    }

    private int getProjectNextNumber(Project project) {
        Optional<Task> optionalTask = taskRepo.findFirstByProjectOrderByNumberDesc(project);
        return optionalTask.map(task -> task.getNumber() + 1).orElse(1);
    }
}
