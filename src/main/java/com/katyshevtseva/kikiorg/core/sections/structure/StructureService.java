package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActivityRepo;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StructureService {
    private final ActivityRepo activityRepo;

    public void createActivity(String title) {
        activityRepo.save(new Activity(title, ActivityStatus.ACTIVE));
    }

    public List<Activity> getActivities(@NotNull ActivityStatus status, @Nullable Goal goal) {
        List<Activity> activities = goal == null ?
                activityRepo.findByStatus(status) :
                activityRepo.findByStatusAndGoal(status, goal);
        return activities.stream().sorted(Comparator.comparing(Activity::getTitle)).collect(Collectors.toList());
    }

    public List<Activity> getActivitiesForActionsSection() {
        //41 = StructureActions
        return activityRepo.findAll();//todo
    }

    public void edit(Activity activity, String title) {//todo
        activity.setTitle(title);
        activityRepo.save(activity);
    }

    public void delete(Activity activity) {
        activityRepo.delete(activity);
    }

    public void setStatus(Activity activity, ActivityStatus status) {
        activity.setStatus(status);
        activityRepo.save(activity);
    }
}
