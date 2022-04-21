package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.polarperiod.PolarPeriod;
import com.katyshevtseva.kikiorg.core.polarperiod.PolarPeriodService;
import com.katyshevtseva.kikiorg.core.polarperiod.TimeUnit;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.history.TargetHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.CourseOfActionRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.katyshevtseva.kikiorg.core.sections.structure.StatusUtils.validateStatusChange;
import static com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus.*;

@Service
@RequiredArgsConstructor
public class TargetService {
    private final TargetRepo targetRepo;
    private final TargetHistoryService targetHistoryService;
    private final TargetChangeActionRepo targetChangeActionRepo;
    private final DateService dateService;
    private final PolarPeriodService polarPeriodService;
    private final CourseOfActionRepo courseOfActionRepo;

    public void create(CourseOfAction courseOfAction, String title, String desc) {
        Target target = targetRepo.save(new Target(title, desc, courseOfAction.getRootTargetGroup(), NEW));
        targetHistoryService.commitCreateAction(target);
    }

    public void edit(Target target, String title, String desc) {
        target.setTitle(title);
        target.setDescription(desc);

        target = targetRepo.save(target);
        targetHistoryService.commitEditedAction(target);
    }

    @Transactional
    public void delete(Target target) {
        targetChangeActionRepo.deleteByTarget(target);
        targetRepo.delete(target);
        polarPeriodService.delete(target.getPeriod());
    }

    public List<Target> getAllStartedTargets() {
        return targetRepo.findByStatus(STARTED);
    }

    public void start(Target target, TimeUnit timeUnit, int period) {
        PolarPeriod polarPeriod = polarPeriodService.save(new PolarPeriod(dateService.createIfNotExistAndGetDateEntity(new Date()), timeUnit, period));
        setStatusAndPeriod(target, STARTED, polarPeriod);
    }

    public void reject(Target target) {
        PolarPeriod period = target.getPeriod();
        setStatusAndPeriod(target, REJECTED, null);
        polarPeriodService.delete(period);
    }

    public void done(Target target) {
        PolarPeriod period = target.getPeriod();
        setStatusAndPeriod(target, DONE, null);
        polarPeriodService.delete(period);
    }

    public String getTargetHierarchyOverviewString(Target target) {
        StringBuilder stringBuilder = new StringBuilder();
        TargetGroup targetGroup = target.getParent();
        while (targetGroup.getParent() != null) {
            stringBuilder.append(targetGroup.getTitle()).append("\n");
            targetGroup = targetGroup.getParent();
        }
        stringBuilder.append(courseOfActionRepo.findByRootTargetGroup(targetGroup).get().getTitle());
        return stringBuilder.toString();
    }

    private void setStatusAndPeriod(Target target, TargetStatus status, PolarPeriod period) {
        validateStatusChange(target.getStatus(), status);

        targetHistoryService.commitChangeStatusAction(target, target.getStatus(), status, period != null ? period.toString() : null);

        target.setPeriod(period);
        target.setStatus(status);
        targetRepo.save(target);
    }
}
