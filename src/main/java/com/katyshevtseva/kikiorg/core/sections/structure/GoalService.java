package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActivityRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.GoalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GoalService {
    private final GoalRepo goalRepo;
    private final ActivityRepo activityRepo;

    public void save(Goal existing, String title, String desc, GoalTopicality topicality) {
        if (existing == null)
            existing = new Goal();
        existing.setValues(title, desc, topicality);
        goalRepo.save(existing);
    }

    public void delete(Goal goal) {
        for (Activity activity : activityRepo.findByGoal(goal)) {
            unattachGoal(activity);
        }
        goalRepo.delete(goal);
    }

    public List<Goal> getAllSortedByTitle() {
        return goalRepo.findAll().stream().sorted(Comparator.comparing(Goal::getTitle)).collect(Collectors.toList());
    }

    public List<Goal> getAllSortedByTopicality() {
        return goalRepo.findAll().stream()
                .sorted(Comparator.comparing(goal -> ((Goal) goal).getTopicality().getIntValue()).reversed())
                .collect(Collectors.toList());
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
