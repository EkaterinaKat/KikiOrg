package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndValueRepo;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndicatorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final IndMarkRepo markRepo;
    private final DateService dateService;
    private final IndicatorRepo indicatorRepo;
    private final IndValueRepo valueRepo;

    public void save(Indicator existing, String title, String desc) {
        if (existing == null) {
            existing = new Indicator();
            existing.setArchived(false);
            existing.setHidden(false);
        }
        existing.setTitle(title.trim());
        existing.setDescription(GeneralUtils.trim(desc));
        indicatorRepo.save(existing);
    }

    public void save(Indicator indicator, IndValue existing, String title, String desc, String color) {
        if (existing == null) {
            existing = new IndValue();
            existing.setDefaultValue(false);
        }
        existing.setIndicator(indicator);
        existing.setTitle(title.trim());
        existing.setDescription(GeneralUtils.trim(desc));
        existing.setColor(GeneralUtils.trim(color));
        valueRepo.save(existing);
    }

    public void makeDefault(IndValue value) {
        List<IndValue> values = valueRepo.findAllByIndicator(value.getIndicator());
        values.forEach(val -> {
            val.setDefaultValue(false);
            valueRepo.save(val);
        });

        if (!value.isDefaultValue()) { //чтобы можно было отменять выбор уже выбранного без выбора нового
            value.setDefaultValue(true);
            valueRepo.save(value);
        }
    }

    public List<Indicator> getAllIndicators() {
        return indicatorRepo.findAllByOrderByTitle().stream()
                .sorted(Comparator.comparing(Indicator::getHidden))
                .sorted(Comparator.comparing(Indicator::getArchived))
                .collect(Collectors.toList());
    }

    public List<Indicator> getActiveIndicators() {
        return indicatorRepo.findAllByArchivedFalseOrderByTitle().stream()
                .sorted(Comparator.comparing(Indicator::getHidden))
                .collect(Collectors.toList());
    }

    public List<Indicator> getActiveNotHiddenIndicators() {
        return indicatorRepo.findAllByArchivedFalseAndHiddenFalseOrderByTitle();
    }

    public List<Indicator> getActiveHiddenIndicators() {
        return indicatorRepo.findAllByArchivedFalseAndHiddenTrueOrderByTitle();
    }


    public List<Indicator> getIndicatorsWithNumericValues() {
        return getActiveIndicators().stream().filter(DiaryUtils::hasNumericValues).collect(Collectors.toList());
    }

    public void saveMark(Indicator indicator, Date date, IndValue value, String comment) {
        if (value != null) {
            validateIndicatorAndValue(indicator, value);
        }

        getMark(indicator, date).ifPresent(markRepo::delete);

        if (value != null || !GeneralUtils.isEmpty(comment)) {
            IndMark mark = new IndMark(
                    indicator,
                    dateService.createIfNotExistAndGetDateEntity(date),
                    value,
                    GeneralUtils.trim(comment));
            markRepo.save(mark);
        }
    }

    public Optional<IndMark> getMark(Indicator indicator, Date date) {
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

    public void archive(Indicator indicator) {
        indicator.setArchived(!indicator.getArchived());
        indicatorRepo.save(indicator);
    }

    public void hide(Indicator indicator) {
        indicator.setHidden(!indicator.getHidden());
        indicatorRepo.save(indicator);
    }

    @Transactional
    public void delete(Indicator indicator) {
        markRepo.deleteByIndicator(indicator);
        valueRepo.deleteByIndicator(indicator);
        indicatorRepo.delete(indicator);
    }

    public void delete(DairyTableService.MarkToEdit mark) {
        if (mark.getMark() != null)
            markRepo.delete(mark.getMark());
    }
}
