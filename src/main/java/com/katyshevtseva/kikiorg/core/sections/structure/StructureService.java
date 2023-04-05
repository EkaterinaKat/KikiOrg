package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActivityRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ParamRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ParamValueRepo;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StructureService {
    private final ActivityRepo activityRepo;
    private final ParamRepo paramRepo;
    private final ParamValueRepo paramValueRepo;

    public void createParam(String title, boolean required, boolean singleValue) {
        paramRepo.save(new Param(title, required, singleValue));
    }

    public void createParamValue(String title, Param param) {
        paramValueRepo.save(new ParamValue(title, param));
    }

    public void createActivity(String title) {
        activityRepo.save(new Activity(title, ActivityStatus.ACTIVE));
    }

    public void addParamValues(Activity activity, Param param, List<ParamValue> values) {
        if (values.stream().anyMatch(paramValue -> !paramValue.getParam().equals(param))) {
            throw new RuntimeException();
        }
        activity.getParamValues().removeAll(param.getValues());
        activity.getParamValues().addAll(values);
        activityRepo.save(activity);
    }

    public List<Activity> getActivities(@NotNull ActivityStatus status, @Nullable Goal goal) {
        List<Activity> activities = goal == null ?
                activityRepo.findByStatus(status) :
                activityRepo.findByStatusAndGoal(status, goal);
        return activities.stream().sorted(Comparator.comparing(Activity::getTitle)).collect(Collectors.toList());
    }

    public List<Activity> getActivitiesForActionsSection() {
        //41 = StructureActions
        return activityRepo.findByParamValue(paramValueRepo.findById(41L).get()).stream()
                .filter(activity -> activity.getStatus() == ActivityStatus.ACTIVE)
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

    public void setStatus(Activity activity, ActivityStatus status) {
        activity.setStatus(status);
        activityRepo.save(activity);
    }

    public String getParamWarningMessageOrNull() {
        for (ParamValue paramValue : paramValueRepo.findAll()) {
            if (activityRepo.findByParamValue(paramValue).isEmpty()) {
                return String.format("Значение %s нигде не используется", paramValue.getTitle());
            }
        }
        return null;
    }
}
