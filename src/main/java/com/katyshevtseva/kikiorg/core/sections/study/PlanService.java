package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Plan;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.PlanRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanService {
    private final PlanRepo repo;
    private final DateService dateService;
    private final StudyService studyService;

    public void save(Plan existing, Subject subject, Date start, Date end, Long minDays, Integer minHourADay) {
        DateEntity startEntity = dateService.createIfNotExistAndGetDateEntity(start);
        DateEntity endEntity = dateService.createIfNotExistAndGetDateEntity(end);
        if (existing == null) {
            existing = new Plan();
            existing.setArchived(false);
        }
        existing.setValues(subject, startEntity, endEntity, minDays, minHourADay.longValue());
        repo.save(existing);
    }

    public void delete(Plan plan) {
        repo.delete(plan);
    }

    public List<Plan> getAllPlans() {
        return repo.findAllByOrderByArchivedAscIdDesc();
    }

    public List<Plan> getActivePlans() {
        return repo.findAllByArchivedFalseOrderByIdDesc();
    }

    public void archive(Plan plan) {
        plan.setArchived(true);
        repo.save(plan);
    }

    public boolean planCompleted(Plan plan) {
        List<SubjMark> marks = getMarksByPlan(plan);
        return marks.stream().filter(mark -> mark.getMinutes() >= plan.getMinMinutesADay()).count() >= plan.getMinDays();
    }

    private List<SubjMark> getMarksByPlan(Plan plan) {
        List<Date> dates = DateUtils.getDateRange(new Period(plan.getStart().getValue(), plan.getEnd().getValue()));
        List<SubjMark> marks = new ArrayList<>();
        for (Date date : dates) {
            studyService.getMark(plan.getSubject(), date).ifPresent(marks::add);
        }
        return marks;
    }
}
