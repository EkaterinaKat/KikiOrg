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
public class ActivityService {
    private final ActivityRepo activityRepo;

    public void save(Activity existing, String title, String desc, Goal goal, PkdType pkdType) {
        if (existing == null) {
            existing = new Activity();
            existing.setStatus(ActivityStatus.ACTIVE);
        }
        existing.setPkdType(pkdType);
        existing.setTitle(title);
        existing.setDescription(desc);
        existing.setGoal(goal);
        activityRepo.save(existing);
    }

    public List<Activity> getActivities(@NotNull ActivityStatus status, @Nullable Goal goal) {
        List<Activity> activities = goal == null ?
                activityRepo.findByStatus(status) :
                activityRepo.findByStatusAndGoal(status, goal);
        return activities.stream().sorted(Comparator.comparing(Activity::getTitle)).collect(Collectors.toList());
    }

    public void delete(Activity activity) {
        activityRepo.delete(activity);
    }

    public void setStatus(Activity activity, ActivityStatus status) {
        activity.setStatus(status);
        activityRepo.save(activity);
    }
}
