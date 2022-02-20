package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.hierarchy.HierarchyService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetRepo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StructureHierarchyService extends HierarchyService<Target, TargetGroup> {
    private final CourseOfAction courseOfAction;
    private final StructureService structureService;
    private final TargetGroupRepo targetGroupRepo;
    private final TargetRepo targetRepo;

    @Override
    protected void createNewGroup(String s) {
        structureService.createTargetGroup(courseOfAction, s, null);
    }

    @Override
    protected void saveModifiedGroup(TargetGroup targetGroup) {
        targetGroupRepo.save(targetGroup);
    }

    @Override
    protected void saveModifiedLeaf(Target target) {
        targetRepo.save(target);
    }

    @Override
    public List<TargetGroup> getAllGroups() {
        return targetGroupRepo.findAllByParent(courseOfAction.getRootTargetGroup());
    }

    @Override
    protected List<Target> getAllLeaves() {
        return targetRepo.findByParent(courseOfAction.getRootTargetGroup());
    }

    @Override
    protected void deleteGroup(TargetGroup targetGroup) {
        structureService.deleteTargetGroup(targetGroup);
    }

    @Override
    protected List<Target> getLeavesByParentGroup(TargetGroup targetGroup) {
        return targetRepo.findByParent(targetGroup);
    }

    @Override
    protected List<TargetGroup> getGroupsByParentGroup(TargetGroup targetGroup) {
        return targetGroupRepo.findAllByParent(targetGroup);
    }
}
