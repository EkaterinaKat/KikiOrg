package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.Sphere;
import com.katyshevtseva.kikiorg.core.sections.structure.history.CourseOfActionHistoryService;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.CourseChangeActionRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.CourseOfActionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseOfActionService {
    private final CourseOfActionRepo courseOfActionRepo;
    private final CourseOfActionHistoryService courseOfActionHistoryService;
    private final CourseChangeActionRepo courseChangeActionRepo;

    private final TargetGroupService targetGroupService;

    public void create(String title, String desc, CourseOfActionStatus status, Sphere sphere) {
        CourseOfAction course = courseOfActionRepo.save(new CourseOfAction(title, desc, targetGroupService.createRootGroup(title), status, sphere));
        courseOfActionHistoryService.createNewAction(course, "CREATED:\n" + course);
    }

    public List<CourseOfAction> getCoursesByStatus(CourseOfActionStatus status) {
        return courseOfActionRepo.findByStatus(status);
    }

    public void edit(CourseOfAction courseOfAction, String title, String desc, CourseOfActionStatus status, Sphere sphere) {
        courseOfAction.setTitle(title);
        courseOfAction.setDescription(desc);
        courseOfAction.setStatus(status);
        courseOfAction.setSphere(sphere);

        courseOfAction = courseOfActionRepo.save(courseOfAction);
        courseOfActionHistoryService.createNewAction(courseOfAction, "EDITED:\n" + courseOfAction);
    }

    public void delete(CourseOfAction courseOfAction) {
        courseChangeActionRepo.deleteByCourseOfAction(courseOfAction);
        courseOfActionRepo.delete(courseOfAction);
    }
}
