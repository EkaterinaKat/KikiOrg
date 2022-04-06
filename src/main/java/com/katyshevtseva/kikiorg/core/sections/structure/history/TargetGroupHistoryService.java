package com.katyshevtseva.kikiorg.core.sections.structure.history;

import com.katyshevtseva.history.HistoryService;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroupChangeAction;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupChangeActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TargetGroupHistoryService extends HistoryService<TargetGroup, TargetGroupChangeAction> {
    private final TargetGroupChangeActionRepo targetGroupChangeActionRepo;
    private final DateService dateService;

    @Override
    protected TargetGroupChangeAction createNewAction() {
        return new TargetGroupChangeAction();
    }

    @Override
    protected void saveNewAction(TargetGroupChangeAction targetGroupChangeAction) {
        targetGroupChangeActionRepo.save(targetGroupChangeAction);
    }

    @Override
    protected void setDate(TargetGroupChangeAction targetGroupChangeAction, Date date) {
        targetGroupChangeAction.setTimestamp(date);
    }
}
