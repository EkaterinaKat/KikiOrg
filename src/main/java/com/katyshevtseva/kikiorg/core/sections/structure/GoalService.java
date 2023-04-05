package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActivityRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.GoalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepo goalRepo;
    private final ActivityRepo activityRepo;

    public void save(Goal existing, String title, String desc) {
        if (existing == null)
            existing = new Goal();
        existing.setValues(title, desc);
        goalRepo.save(existing);
    }

    public void delete(Goal goal) {
        for (Activity activity : activityRepo.findByGoal(goal)) {
            unattachGoal(activity);
        }
        goalRepo.delete(goal);
    }

    public List<Goal> getAll() {
        return goalRepo.findAll();
    }

    public void unattachGoal(Activity activity) {
        activity.setGoal(null);
        activityRepo.save(activity);
    }

    public void attach(Activity activity, Goal goal) {
        activity.setGoal(goal);
        activityRepo.save(activity);
    }
}
