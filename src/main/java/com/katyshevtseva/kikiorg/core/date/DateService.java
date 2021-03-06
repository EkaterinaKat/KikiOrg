package com.katyshevtseva.kikiorg.core.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.katyshevtseva.kikiorg.core.date.DateUtils.getDateRange;

@Service
public class DateService {
    @Autowired
    private DateRepo dateRepo;

    public DateEntity createIfNotExistAndGetDateEntity(Date date) {
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
        for (Date date : getDateRange(period))
            dateRepo.findByValue(date).ifPresent(dateEntities::add);
        return dateEntities;
    }
}
