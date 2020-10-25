package com.katyshevtseva.kikiorg.core.date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DateService {
    @Autowired
    private DateRepo dateRepo;

    public DateEntity getDateEntity(Date date){
        Optional<DateEntity> optionalDateEntity = dateRepo.findByValue(date);
        if (optionalDateEntity.isPresent())
            return optionalDateEntity.get();

        DateEntity dateEntity = new DateEntity();
        dateEntity.setValue(date);
        return dateRepo.save(dateEntity);
    }
}
