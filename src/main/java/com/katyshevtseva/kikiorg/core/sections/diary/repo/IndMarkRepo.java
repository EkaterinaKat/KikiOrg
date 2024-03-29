package com.katyshevtseva.kikiorg.core.sections.diary.repo;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface IndMarkRepo extends JpaRepository<IndMark, Long> {

    List<IndMark> findByIndicatorAndDateEntity(Indicator indicator, DateEntity dateEntity);

    List<IndMark> findByIndicator(Indicator indicator);

    List<IndMark> findByIndicatorOrderByDateEntityValue(Indicator indicator);

    void deleteByIndicator(Indicator indicator);

    IndMark findFirstByIndicatorOrderByDateEntityValue(Indicator indicator);

    boolean existsByIndicatorAndDateEntityValue(Indicator indicator, Date date);
}
