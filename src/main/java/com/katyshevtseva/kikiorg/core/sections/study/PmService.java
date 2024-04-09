package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.PlanMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.PlanMarkRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PmService {
    private final PlanMarkRepo repo;
    private final DateService dateService;

    public boolean markExists(Subject subject, Date date) {
        return repo.existsByDateEntityValueAndSubject(date, subject);
    }

    @Transactional
    public void makeMark(Subject subject, Date date) {
        Optional<PlanMark> mark = repo.findByDateEntityValueAndSubject(date, subject);
        if (mark.isPresent()) {
            repo.delete(mark.get());
        } else {
            repo.save(new PlanMark(subject, dateService.createIfNotExistAndGetDateEntity(date)));
        }
    }
}
