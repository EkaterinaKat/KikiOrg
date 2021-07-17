package com.katyshevtseva.kikiorg.core.date;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DateService {
    private final DateRepo dateRepo;

    public DateEntity createIfNotExistAndGetDateEntity(Date date) {
        if (date == null)
            return null;

        Optional<DateEntity> optionalDateEntity = dateRepo.findByValue(date);
        if (optionalDateEntity.isPresent())
            return optionalDateEntity.get();

        DateEntity dateEntity = new DateEntity();
        dateEntity.setValue(date);
        return dateRepo.save(dateEntity);
    }

    public DateEntity getDateEntityIfExistsOrNull(Date date) {
        return dateRepo.findByValue(date).orElse(null);
    }

    public List<DateEntity> getOnlyExistingDateEntitiesByPeriod(Period period) {
        List<DateEntity> dateEntities = new ArrayList<>();
        for (Date date : DateUtils.getDateRange(period))
            dateRepo.findByValue(date).ifPresent(dateEntities::add);
        return dateEntities;
    }
}
