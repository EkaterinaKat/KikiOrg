package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.Sphere;
import com.katyshevtseva.kikiorg.core.sections.structure.history.CourseOfActionHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.history.TargetGroupHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.history.TargetHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus.NEW;

@Service
@RequiredArgsConstructor
public class StructureService {
    private final TargetGroupRepo targetGroupRepo;
    private final CourseOfActionRepo courseOfActionRepo;
    private final TargetRepo targetRepo;
    private final TargetGroupHistoryService targetGroupHistoryService;
    private final TargetHistoryService targetHistoryService;
    private final CourseOfActionHistoryService courseOfActionHistoryService;
    private final TargetGroupChangeActionRepo targetGroupChangeActionRepo;
    private final TargetChangeActionRepo targetChangeActionRepo;
    private final CourseChangeActionRepo courseChangeActionRepo;

    public void createCourceOfAction(String title, String desc, CourseOfActionStatus status, Sphere sphere) {
        TargetGroup rootGroup = targetGroupRepo.save(new TargetGroup(title + " root", null, null, NEW));
        targetGroupHistoryService.createNewAction(rootGroup, "CREATED:\n" + rootGroup);

        CourseOfAction course = courseOfActionRepo.save(new CourseOfAction(title, desc, rootGroup, status, sphere));
        courseOfActionHistoryService.createNewAction(course, "CREATED:\n" + course);
    }

    public void createTargetGroup(CourseOfAction courseOfAction, String title, String desc) {
        TargetGroup targetGroup = targetGroupRepo.save(new TargetGroup(title, desc, courseOfAction.getRootTargetGroup(), NEW));
        targetGroupHistoryService.createNewAction(targetGroup, "CREATED:\n" + targetGroup);
    }

    public void createTarget(CourseOfAction courseOfAction, String title, String desc) {
        Target target = targetRepo.save(new Target(title, desc, courseOfAction.getRootTargetGroup(), NEW));
        targetHistoryService.createNewAction(target, "CREATED:\n" + target);
    }

    public List<CourseOfAction> getCoursesByStatus(CourseOfActionStatus status) {
        return courseOfActionRepo.findByStatus(status);
    }

    public void editCourceOfAction(CourseOfAction courseOfAction, String title, String desc, CourseOfActionStatus status, Sphere sphere) {
        courseOfAction.setTitle(title);
        courseOfAction.setDescription(desc);
        courseOfAction.setStatus(status);
        courseOfAction.setSphere(sphere);

        courseOfAction = courseOfActionRepo.save(courseOfAction);
        courseOfActionHistoryService.createNewAction(courseOfAction, "EDITED:\n" + courseOfAction);
    }

    public void editTargetGroup(TargetGroup targetGroup, String title, String desc) {
        targetGroup.setTitle(title);
        targetGroup.setDescription(desc);

        targetGroup = targetGroupRepo.save(targetGroup);
        targetGroupHistoryService.createNewAction(targetGroup, "EDITED:\n" + targetGroup);
    }

    public void editTarget(Target target, String title, String desc) {
        target.setTitle(title);
        target.setDescription(desc);

        target = targetRepo.save(target);
        targetHistoryService.createNewAction(target, "EDITED:\n" + target);
    }

    public void deleteTargetGroup(TargetGroup targetGroup) {
        targetGroupChangeActionRepo.deleteByTargetGroup(targetGroup);
        targetGroupRepo.delete(targetGroup);
    }

    public void deleteTarget(Target target) {
        targetChangeActionRepo.deleteByTarget(target);
        targetRepo.delete(target);
    }

    public void deleteCourseOfAction(CourseOfAction courseOfAction) {
        courseChangeActionRepo.deleteByCourseOfAction(courseOfAction);
        courseOfActionRepo.delete(courseOfAction);
    }
}
