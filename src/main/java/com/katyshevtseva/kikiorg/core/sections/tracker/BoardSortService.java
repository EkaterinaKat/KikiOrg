package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.sections.tracker.repo.TaskRepo;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardSortService {
    private final TaskRepo taskRepo;

    public enum SortType {
        CREATION_DATE, COMPLETION_DATE
    }

    public Page<Task> getTaskPage(Project project, TaskStatus status, SortType sortType, int pageNum) {
        String sort = null;

        switch (sortType) {
            case CREATION_DATE:
                sort = "id";
                break;
            case COMPLETION_DATE:
                sort = "completionDate.value";
        }

        PageRequest pageRequest = PageRequest.of(pageNum, 10, Sort.by(sort).descending());

        org.springframework.data.domain.Page<Task> taskPage = project != null ?
                taskRepo.findByTaskStatusAndProject(status, project, pageRequest) :
                taskRepo.findByTaskStatus(status, pageRequest);
        return new Page<>(taskPage.getContent(), pageNum, taskPage.getTotalPages());
    }
}
