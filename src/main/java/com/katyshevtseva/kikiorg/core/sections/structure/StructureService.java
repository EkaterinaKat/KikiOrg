package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Param;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.ParamValue;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ActivityRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ParamRepo;
import com.katyshevtseva.kikiorg.core.sections.structure.repo.ParamValueRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StructureService {
    private final ActivityRepo activityRepo;
    private final ParamRepo paramRepo;
    private final ParamValueRepo paramValueRepo;

    public Param createParam(String title) {
        return paramRepo.save(new Param(title));
    }

    public ParamValue createParamValue(String title, Param param) {
        return paramValueRepo.save(new ParamValue(title, param));
    }

    public Activity createActivity(String title) {
        return activityRepo.save(new Activity(title));
    }

    public void addParamValue(Activity activity, ParamValue paramValue) {
        activity.getParamValues().add(paramValue);
        activityRepo.save(activity);
    }

    public List<Activity> getActivities() {
        return activityRepo.findAll();
    }

    public List<Param> getParams() {
        return paramRepo.findAll();
    }

    public void edit(Param param, String title) {
        param.setTitle(title);
        paramRepo.save(param);
    }

    public void edit(ParamValue paramValue, String title) {
        paramValue.setTitle(title);
        paramValueRepo.save(paramValue);
    }

    public void delete(Param param) {
        param.getValues().forEach(this::delete);
        param.setValues(new HashSet<>());
        paramRepo.delete(param);
    }

    public void delete(ParamValue paramValue) {
        //todo убедиться что метод findByParamValues работает
        for (Activity activity: activityRepo.findByParamValues(paramValue)) {
            activity.getParamValues().remove(paramValue);
            activityRepo.save(activity);
        }
        paramValueRepo.delete(paramValue);
    }
}
