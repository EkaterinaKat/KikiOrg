package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.hierarchy.HierarchyService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetRepo;
import lombok.RequiredArgsConstructor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RequiredArgsConstructor
public class StructureHierarchyService extends HierarchyService<Target, TargetGroup> {
    private final CourseOfAction courseOfAction;
    private final TargetGroupRepo targetGroupRepo;
    private final TargetRepo targetRepo;

    @Override
    protected void createNewGroup(String s) {
        throw new NotImplementedException();
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
        // список всех групп нужен для заполнения таблички с группами, которая в этом модуле отключена
        // и еще этот метод используется в дефолтной реализации getTopLevelGroups которая в этом классе переопределена
        // соответственно этот метод нам не нужен
        throw new NotImplementedException();
    }

    @Override
    protected List<Target> getAllLeaves() {
        // список всех листьев используется в дефолтной реализации getTopLevelLeaves которая в этом классе переопределена
        // соответственно этот метод нам не нужен
        throw new NotImplementedException();
    }

    @Override
    protected void deleteGroup(TargetGroup targetGroup) {
        throw new NotImplementedException();
    }

    @Override
    protected List<Target> getLeavesByParentGroup(TargetGroup targetGroup) {
        return targetRepo.findByParent(targetGroup);
    }

    @Override
    protected List<TargetGroup> getGroupsByParentGroup(TargetGroup targetGroup) {
        return targetGroupRepo.findAllByParent(targetGroup);
    }

    @Override
    protected List<Target> getTopLevelLeaves() {
        return targetRepo.findByParent(courseOfAction.getRootTargetGroup());
    }

    @Override
    protected List<TargetGroup> getTopLevelGroups() {
        return targetGroupRepo.findAllByParent(courseOfAction.getRootTargetGroup());
    }

    @Override
    public boolean isTopLevel(HierarchyNode node) {
        return node.getParentGroup().equals(courseOfAction.getRootTargetGroup());
    }

    @Override
    public void deleteFromSchema(HierarchyNode node) {
        node.setParentGroup(courseOfAction.getRootTargetGroup());
        saveModifiedNode(node);
    }

    public boolean nodeBelongToCourse(HierarchyNode node) {
        return treeWithRootContainsNode(courseOfAction.getRootTargetGroup(), node);
    }
}
