package com.katyshevtseva.kikiorg.core.sections.structure.history;

import com.katyshevtseva.history.HistoryService;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseChangeAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.CourseChangeActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CourseOfActionHistoryService extends HistoryService<CourseOfAction, CourseChangeAction> {
    private final CourseChangeActionRepo courseChangeActionRepo;
    private final DateService dateService;

    @Override
    protected CourseChangeAction createNewAction() {
        return new CourseChangeAction();
    }

    @Override
    protected void saveNewAction(CourseChangeAction courseChangeAction) {
        courseChangeActionRepo.save(courseChangeAction);
    }

    @Override
    protected void setDate(CourseChangeAction courseChangeAction, Date date) {
        courseChangeAction.setTimestamp(date);
    }
}
