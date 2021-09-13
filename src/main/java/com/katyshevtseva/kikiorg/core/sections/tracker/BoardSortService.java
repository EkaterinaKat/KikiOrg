package com.katyshevtseva.kikiorg.core.sections.tracker;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.repo.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardSortService {
    private final TaskRepo taskRepo;

    public enum SortType {
        PROJECT, CREATION_DATE, COMPLETION_DATE
    }

    public Page<Task> getTaskPage(TaskStatus status, SortType sortType, int pageNum) {
        String sort = null;

        switch (sortType) {
            case PROJECT:
                sort = "project.title";
                break;
            case CREATION_DATE:
                sort = "creationDate.value";
                break;
            case COMPLETION_DATE:
                sort = "completionDate.value";
        }

        org.springframework.data.domain.Page<Task> taskPage =
                taskRepo.findByTaskStatus(status, PageRequest.of(pageNum, 10, Sort.by(sort).descending()));
        return new Page<>(taskPage.getContent(), pageNum, taskPage.getTotalPages());
    }
}
