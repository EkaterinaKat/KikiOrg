package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.kikiorg.core.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSortService {
    private final TaskRepo taskRepo;

    public enum SortType {
        PROJECT, CREATION_DATE, COMPLETION_DATE
    }

    public List<Task> getTodoTasks(SortType sortType) {
        List<Task> tasks = taskRepo.findAllByTaskStatus(TaskStatus.TODO);

        switch (sortType) {
            case PROJECT:
            case COMPLETION_DATE:
                tasks.sort(Comparator.comparing(Task::getProject).thenComparing(Task::getCreationDate));
                break;
            case CREATION_DATE:
                tasks.sort(Comparator.comparing(Task::getCreationDate).thenComparing(Task::getProject));
        }
        return tasks;
    }

    public List<Task> getShelvedTasks(SortType sortType) {
        List<Task> tasks = taskRepo.findAllByTaskStatus(TaskStatus.SHELVED);

        switch (sortType) {
            case PROJECT:
            case COMPLETION_DATE:
                tasks.sort(Comparator.comparing(Task::getProject).thenComparing(Task::getCreationDate));
                break;
            case CREATION_DATE:
                tasks.sort(Comparator.comparing(Task::getCreationDate).thenComparing(Task::getProject));
        }
        return tasks;
    }

    public List<Task> getAllDoneAndRejectedTasks(SortType sortType) {
        List<Task> tasks = taskRepo.findAllByTaskStatus(TaskStatus.REJECTED);
        tasks.addAll(taskRepo.findAllByTaskStatus(TaskStatus.DONE));

        switch (sortType) {
            case PROJECT:
                tasks.sort(Comparator.comparing(Task::getProject).thenComparing(Task::getCreationDate));
                break;
            case COMPLETION_DATE:
                tasks.sort(Comparator.comparing(Task::getCompletionDate).thenComparing(Task::getProject));
                break;
            case CREATION_DATE:
                tasks.sort(Comparator.comparing(Task::getCreationDate).thenComparing(Task::getProject));
        }
        return tasks;
    }
}
