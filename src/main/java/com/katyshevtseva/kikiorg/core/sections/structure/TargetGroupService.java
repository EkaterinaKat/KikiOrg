package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.history.TargetGroupHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.katyshevtseva.kikiorg.core.sections.structure.StatusUtils.validateStatusChange;
import static com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus.*;

@Service
@RequiredArgsConstructor
public class TargetGroupService {
    private final TargetGroupRepo targetGroupRepo;
    private final TargetGroupHistoryService targetGroupHistoryService;
    private final TargetGroupChangeActionRepo targetGroupChangeActionRepo;

    public TargetGroup createRootGroup(String courseOfActionTitle) {
        return targetGroupRepo.save(new TargetGroup(courseOfActionTitle + " root", null, null, NEW));
    }

    public TargetGroup create(CourseOfAction courseOfAction, String title, String desc) {
        TargetGroup targetGroup = targetGroupRepo.save(new TargetGroup(title, desc, courseOfAction.getRootTargetGroup(), NEW));
        targetGroupHistoryService.commitCreateAction(targetGroup);
        return targetGroup;
    }

    public void edit(TargetGroup targetGroup, String title, String desc) {
        targetGroup.setTitle(title);
        targetGroup.setDescription(desc);

        targetGroup = targetGroupRepo.save(targetGroup);
        targetGroupHistoryService.commitEditedAction(targetGroup);
    }

    @Transactional
    public void moveChildrenToRootAndDeleteGroup(TargetGroup groupToDelete, CourseOfAction course) {
        StructureHierarchyService hierarchyService = Core.getInstance().structureHierarchyService(course);
        for (HierarchyNode node : hierarchyService.getNodesByParent(groupToDelete)) {
            hierarchyService.deleteFromSchema(node);
        }
        delete(groupToDelete);
    }

    @Transactional
    public void delete(TargetGroup targetGroup) {
        targetGroupChangeActionRepo.deleteByTargetGroup(targetGroup);
        targetGroupRepo.delete(targetGroup);
    }

    public void reject(TargetGroup targetGroup) {
        changeStatus(targetGroup, REJECTED);
    }

    public void start(TargetGroup targetGroup) {
        changeStatus(targetGroup, STARTED);
    }

    public void done(TargetGroup targetGroup) {
        changeStatus(targetGroup, DONE);
    }

    @Transactional
    private void changeStatus(TargetGroup targetGroup, TargetStatus status) {
        validateStatusChange(targetGroup.getStatus(), status);

        targetGroupHistoryService.commitChangeStatusAction(targetGroup, targetGroup.getStatus(), status);

        targetGroup.setStatus(status);
        targetGroupRepo.save(targetGroup);
    }
}
