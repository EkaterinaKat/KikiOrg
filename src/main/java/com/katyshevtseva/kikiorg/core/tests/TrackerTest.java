package com.katyshevtseva.kikiorg.core.tests;

import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.TaskStatusChangeAction;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackerTest implements TestClass {
    private final TaskRepo taskRepo;

    @Override
    public boolean test() {
        for (Task task : taskRepo.findAll()) {
            List<TaskStatusChangeAction> history = task.getHistory();
            if (history.isEmpty()) {
                if (task.getStatus() != TaskStatus.TODO) {
                    System.out.println(task.getTitle() + " history.isEmpty()&&task.getStatus()!= TaskStatus.TODO");
                    return false;
                }
            } else {
                TaskStatusChangeAction lastAction = history.stream()
                        .max(Comparator.comparing(TaskStatusChangeAction::getId)).get();
                if (lastAction.getStatus() != task.getStatus()) {
                    System.out.println(task.getTitle() + " lastAction.getStatus()!=task.getStatus()");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getTitle() {
        return "TrackerTest";
    }
}
