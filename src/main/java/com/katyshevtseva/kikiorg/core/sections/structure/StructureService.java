package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActivityRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.GoalRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ParamRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ParamValueRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StructureService {
    private final ActivityRepo activityRepo;
    private final ParamRepo paramRepo;
    private final ParamValueRepo paramValueRepo;
    private final DateService dateService;
    private final GoalRepo goalRepo;

    public void createParam(String title, boolean required, boolean singleValue) {
        paramRepo.save(new Param(title, required, singleValue));
    }

    public void createParamValue(String title, Param param) {
        paramValueRepo.save(new ParamValue(title, param));
    }

    public void createActivity(String title) {
        activityRepo.save(new Activity(title));
    }

    public void addParamValues(Activity activity, Param param, List<ParamValue> values) {
        if (values.stream().anyMatch(paramValue -> !paramValue.getParam().equals(param))) {
            throw new RuntimeException();
        }
        activity.getParamValues().removeAll(param.getValues());
        activity.getParamValues().addAll(values);
        activityRepo.save(activity);
    }

    public List<Activity> getActivities(List<ParamValue> valuesToSearchBy) {
        List<Activity> activities = valuesToSearchBy == null || valuesToSearchBy.isEmpty() ?
                activityRepo.findAll() : activityRepo.findByParamValues(valuesToSearchBy);
        return activities.stream().sorted(Comparator.comparing(Activity::getTitle)).collect(Collectors.toList());
    }

    public List<Activity> getActivitiesForGoalsSection() {
        //41 = StructureGoals 21=Активно
        return activityRepo.findByParamValue(paramValueRepo.findById(41L).get()).stream()
                .filter(activity -> activity.getParamValues().contains(paramValueRepo.findById(21L).get()))
                .collect(Collectors.toList());
    }

    public List<Param> getParams() {
        return paramRepo.findAll();
    }

    public void edit(Param param, String title, boolean required, boolean singleValue) {
        param.setTitle(title);
        param.setRequired(required);
        param.setSingleValue(singleValue);
        paramRepo.save(param);
    }

    public void edit(ParamValue paramValue, String title) {
        paramValue.setTitle(title);
        paramValueRepo.save(paramValue);
    }

    public void edit(Activity activity, String title) {
        activity.setTitle(title);
        activityRepo.save(activity);
    }

    public void delete(Param param) {
        param.getValues().forEach(this::delete);
        param.setValues(new HashSet<>());
        paramRepo.delete(param);
    }

    public void delete(ParamValue paramValue) {
        for (Activity activity : activityRepo.findByParamValue(paramValue)) {
            activity.getParamValues().remove(paramValue);
            activityRepo.save(activity);
        }
        paramValueRepo.delete(paramValue);
    }

    public void delete(Activity activity) {
        activityRepo.delete(activity);
    }

    public String getParamWarningMessageOrNull() {
        for (ParamValue paramValue : paramValueRepo.findAll()) {
            if (activityRepo.findByParamValue(paramValue).isEmpty()) {
                return String.format("Значение %s нигде не используется", paramValue.getTitle());
            }
        }
        return null;
    }

    public List<ParamValue> getAllParamValues() {
        return paramValueRepo.findAllByOrderByTitle();
    }

    public void createGoal(Activity activity, String title) {
        Goal goal = new Goal();
        goal.setActivity(activity);
        goal.setTitle(title);
        goal.setCreationDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        goal.setHighlighted(false);
        goalRepo.save(goal);
    }

    public void edit(Goal goal, String title) {
        goal.setTitle(title);
        goalRepo.save(goal);
    }

    public void delete(Goal goal) {
        goalRepo.delete(goal);
    }

    public void done(Goal goal) {
        goal.setCompletionDate(dateService.createIfNotExistAndGetDateEntity(new Date()));
        goalRepo.save(goal);
    }

    public Page<Goal> getTodoGoals(Activity activity, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("id").ascending());

        org.springframework.data.domain.Page<Goal> goalPage =
                goalRepo.findByActivityAndCompletionDateIsNull(activity, pageRequest);
        return new Page<>(goalPage.getContent(), pageNum, goalPage.getTotalPages());
    }

    public Page<Goal> getDoneGoals(Activity activity, int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("id").ascending());

        org.springframework.data.domain.Page<Goal> goalPage =
                goalRepo.findByActivityAndCompletionDateIsNotNull(activity, pageRequest);
        return new Page<>(goalPage.getContent(), pageNum, goalPage.getTotalPages());
    }

    public void highlight(Goal goal) {
        goal.setHighlighted(!goal.isHighlighted());
        goalRepo.save(goal);
    }

    public List<Goal> getHighlightedGoals() {
        return goalRepo.findByHighlightedIsTrue();
    }
}
