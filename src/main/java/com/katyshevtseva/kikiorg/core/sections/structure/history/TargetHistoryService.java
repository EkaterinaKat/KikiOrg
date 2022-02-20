package com.katyshevtseva.kikiorg.core.sections.structure.history;

import com.katyshevtseva.history.HistoryService;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetChangeAction;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetChangeActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TargetHistoryService extends HistoryService<Target, TargetChangeAction> {
    private final TargetChangeActionRepo targetChangeActionRepo;
    private final DateService dateService;

    @Override
    protected TargetChangeAction createNewAction() {
        return new TargetChangeAction();
    }

    @Override
    protected void saveNewAction(TargetChangeAction targetChangeAction) {
        targetChangeActionRepo.save(targetChangeAction);
    }

    @Override
    protected void setDate(TargetChangeAction targetChangeAction, Date date) {
        targetChangeAction.setDateEntity(dateService.createIfNotExistAndGetDateEntity(date));
    }
}
