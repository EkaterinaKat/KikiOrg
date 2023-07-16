package com.katyshevtseva.kikiorg.core.sections.dtt;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService.SortType.CREATION_DATE;
import static com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus.TODO;

@RequiredArgsConstructor
@Service
public class FakeService {
    private static final String PROG_TRACKER_SPHERE_TITLE = "Программирование";
    private final BoardSortService boardService;

    public Sphere getProgTrackerFakeSphere() {
        Sphere sphere = new Sphere();
        sphere.setFake(true);
        sphere.setActive(true);
        sphere.setTitle(PROG_TRACKER_SPHERE_TITLE);
        return sphere;
    }

    public void antiFakeCheck(Sphere sphere) {
        if (sphere.getFake()) {
            throw new RuntimeException();
        }
    }

    public void antiFakeCheck(DatelessTask task) {
        if (task.getFake()) {
            throw new RuntimeException();
        }
    }

    private void fakeCheck(Sphere sphere) {
        if (!sphere.getFake() || !sphere.getTitle().equals(PROG_TRACKER_SPHERE_TITLE)) {
            throw new RuntimeException();
        }
    }

    public Page<DatelessTask> getFakeTodoTasks(Sphere sphere, int pageNum, int pageSize) {
        fakeCheck(sphere);

        Page<Task> taskPage = boardService.getTaskPage(null, TODO, CREATION_DATE, pageNum, pageSize);
        List<DatelessTask> content = taskPage.getContent().stream()
                .map(this::trackerTaskToDatelessTask)
                .collect(Collectors.toList());
        return new Page<>(content, taskPage.getPageNum(), taskPage.getTotalPages());
    }

    public Page<DatelessTask> getFakeDoneTasks(Sphere sphere) {
        fakeCheck(sphere);
        return new Page<>(new ArrayList<>(), 0, 0);
    }

    public List<DatelessTask> getOldestTasks() {
        return boardService.getOldestTasks(2).stream()
                .map(this::trackerTaskToDatelessTask)
                .collect(Collectors.toList());
    }

    private DatelessTask trackerTaskToDatelessTask(Task task) {
        DatelessTask datelessTask = new DatelessTask();
        datelessTask.setFake(true);
        datelessTask.setTitle(task.getNumberAndTitleInfo());
        datelessTask.setCreationDate(task.getCreationDate());
        return datelessTask;
    }
}
