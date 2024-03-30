package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Plan;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.PlanRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanService {
    private final PlanRepo repo;
    private final DateService dateService;

    public void save(Plan existing, Subject subject, Date start, Date end, Integer minDays, Integer minHourADay) {
        DateEntity startEntity = dateService.createIfNotExistAndGetDateEntity(start);
        DateEntity endEntity = dateService.createIfNotExistAndGetDateEntity(end);
        if (existing == null) {
            existing = new Plan();
            existing.setArchived(false);
        }
        existing.setValues(subject, startEntity, endEntity, minDays, minHourADay);
        repo.save(existing);
    }

    public void delete(Plan plan) {
        repo.delete(plan);
    }

    public List<Plan> getAllPlans() {
        return repo.findAll();
    }

    public List<Plan> getActivePlans() {
        return repo.findAllByArchivedFalse();
    }

    public void archive(Plan plan) {
        plan.setArchived(true);
        repo.save(plan);
    }
}
