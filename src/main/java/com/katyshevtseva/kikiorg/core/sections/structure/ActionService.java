package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Action;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ActionService {
    private final DateService dateService;
    private final ActionRepo actionRepo;

    public void createAction(Activity activity, String title) {
        Action action = new Action();
        action.setActivity(activity);
        action.setTitle(title);
        action.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        actionRepo.save(action);
    }

    public void edit(Action action, String title) {
        action.setTitle(title);
        actionRepo.save(action);
    }

    public void delete(Action action) {
        actionRepo.delete(action);
    }

    public void done(Action action) {
        action.setCompletionDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        actionRepo.save(action);
    }

    public Page<Action> getTodoActions(Activity activity, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("id").ascending());

        org.springframework.data.domain.Page<Action> actionPage =
                actionRepo.findByActivityAndCompletionDateIsNull(activity, pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }

    public Page<Action> getDoneActions(Activity activity, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("id").ascending());

        org.springframework.data.domain.Page<Action> actionPage =
                actionRepo.findByActivityAndCompletionDateIsNotNull(activity, pageRequest);
        return new Page<>(actionPage.getContent(), pageNum, actionPage.getTotalPages());
    }

    public String getActionStatistics() {
        return String.format("Todo:%d Done: %d",
                actionRepo.countByCompletionDateIsNull(),
                actionRepo.countByCompletionDateIsNotNull());
    }
}
