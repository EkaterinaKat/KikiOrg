package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndValueRepo;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndicatiorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final IndMarkRepo markRepo;
    private final DateService dateService;
    private final IndicatiorRepo indicatiorRepo;
    private final IndValueRepo valueRepo;

    public void save(Indicator existing, String title) {
        if (existing == null) {
            existing = new Indicator();
        }
        existing.setTitle(title);
        indicatiorRepo.save(existing);
    }

    public void save(Indicator indicator, IndValue existing, String title, String desc) {
        if (existing == null) {
            existing = new IndValue();
        }
        existing.setIndicator(indicator);
        existing.setTitle(title);
        existing.setDescription(desc);
        valueRepo.save(existing);
    }

    public List<Indicator> getIndicators() {
        return indicatiorRepo.findAll();
    }

    public void saveMark(Indicator indicator, Date date, IndValue value, String comment) {
        if (value != null) {
            validateIndicatorAndValue(indicator, value);
        }
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        IndMark mark = getMarkOrNull(indicator, date).orElse(new IndMark(indicator, dateEntity));
        mark.setValue(value);
        mark.setComment(comment);
        markRepo.save(mark);
    }

    public Optional<IndMark> getMarkOrNull(Indicator indicator, Date date) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<IndMark> marks = markRepo.findByIndicatorAndDateEntity(indicator, dateEntity);
        if (marks.size() > 1) {
            throw new RuntimeException();
        }
        return marks.isEmpty() ? Optional.empty() : Optional.ofNullable(marks.get(0));
    }

    private void validateIndicatorAndValue(Indicator indicator, IndValue value) {
        if (!indicator.getValues().contains(value)) {
            throw new RuntimeException();
        }
    }
}
