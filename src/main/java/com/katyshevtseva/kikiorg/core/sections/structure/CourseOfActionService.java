package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.Sphere;
import com.katyshevtseva.kikiorg.core.sections.structure.history.CourseOfActionHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.CourseChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.CourseOfActionRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetGroupRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.TargetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseOfActionService {
    private final CourseOfActionRepo courseOfActionRepo;
    private final CourseOfActionHistoryService courseOfActionHistoryService;
    private final CourseChangeActionRepo courseChangeActionRepo;
    private final TargetService targetService;
    private final TargetGroupService targetGroupService;
    private final TargetGroupRepo targetGroupRepo;
    private final TargetRepo targetRepo;

    public CourseOfAction create(String title, String desc, CourseOfActionStatus status, Sphere sphere) {
        CourseOfAction course = courseOfActionRepo.save(new CourseOfAction(title, desc, targetGroupService.createRootGroup(title), status, sphere));
        courseOfActionHistoryService.commitCreateAction(course);
        return course;
    }

    public Page<CourseOfAction> getCoursesByStatus(CourseOfActionStatus status, int pageNum) {
        org.springframework.data.domain.Page<CourseOfAction> courseOfActionPage =
                courseOfActionRepo.findByStatus(status, PageRequest.of(pageNum, 20));
        return new Page<>(courseOfActionPage.getContent(), pageNum, courseOfActionPage.getTotalPages());
    }

    public CourseOfAction edit(CourseOfAction courseOfAction, String title, String desc, CourseOfActionStatus status, Sphere sphere) {
        if (courseOfAction.getStatus() != status) {
            courseOfActionHistoryService.commitChangeStatusAction(courseOfAction, courseOfAction.getStatus(), status);
            courseOfAction.setStatus(status);
        }
        if (!courseOfAction.getTitle().equals(title)
                || !courseOfAction.getDescription().equals(desc)
                || !courseOfAction.getSphere().equals(sphere)) {
            courseOfAction.setTitle(title);
            courseOfAction.setDescription(desc);
            courseOfAction.setSphere(sphere);
            courseOfActionHistoryService.commitEditedAction(courseOfAction);
        }

        courseOfActionRepo.save(courseOfAction);
        return courseOfActionRepo.findById(courseOfAction.getId()).get();
    }

    @Transactional
    public void delete(CourseOfAction courseOfAction) {
        TargetGroup rootGroup = courseOfAction.getRootTargetGroup();
        StructureHierarchyService hierarchyService = new StructureHierarchyService(courseOfAction, targetGroupRepo, targetRepo);

        courseChangeActionRepo.deleteByCourseOfAction(courseOfAction);
        courseOfActionRepo.delete(courseOfAction);

        for (Target target : hierarchyService.getAllDescendantLeavesByHierarchyNode(rootGroup)) {
            targetService.delete(target);
        }

        List<TargetGroup> allGroups = hierarchyService.getAllDescendantGroupsByParentGroup(rootGroup);
        for (TargetGroup group : allGroups) {
            group.setParent(null);
        }
        for (TargetGroup group : allGroups) {
            targetGroupService.delete(group);
        }
    }
}
