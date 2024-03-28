package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Circs;
import com.katyshevtseva.kikiorg.core.sections.study.enums.CircsType;
import com.katyshevtseva.kikiorg.core.sections.study.repo.CircsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CircsService {
    private final CircsRepo repo;
    private final DateService dateService;

    @Transactional
    public void save(CircsType type, Date date, String comment) {
        if (repo.existsByDateEntityValue(date)) {
            delete(date);
        }
        repo.save(new Circs(dateService.createIfNotExistAndGetDateEntity(date), type, comment));
    }

    @Transactional
    public void delete(Date date) {
        repo.deleteByDateEntityValue(date);
    }

    public Circs getCircsOrNull(Date date) {
        List<Circs> circsList = repo.findByDateEntityValue(date);
        if (circsList.size() == 0) {
            return null;
        }
        if (circsList.size() > 1) {
            throw new RuntimeException();
        }
        return circsList.get(0);
    }
}
