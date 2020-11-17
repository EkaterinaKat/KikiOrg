package com.katyshevtseva.kikiorg.core.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<DateEntity> getOnlyExistingDateEntitiesByPeriod(Date start, Date end) {
        List<DateEntity> dateEntities = new ArrayList<>();
        for (Date date : getDateRange(start, end))
            dateRepo.findByValue(date).ifPresent(dateEntities::add);
        return dateEntities;
    }

    public List<Date> getDateRange(Date start, Date end) {
        Date date = new Date(start.getTime());
        Date oneDayAfterEnd = addOneDay(end);

        List<Date> result = new ArrayList<>();
        while (date.before(oneDayAfterEnd)) {
            result.add(date);
            date = addOneDay(date);
        }
        return result;
    }

    private Date addOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
