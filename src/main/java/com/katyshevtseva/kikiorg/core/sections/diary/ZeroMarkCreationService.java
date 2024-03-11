package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndicatorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ZeroMarkCreationService {
    private final IndMarkRepo markRepo;
    private final DateService dateService;
    private final IndicatorRepo indicatorRepo;

    void createZeroMarks(Indicator indicator) {
        indicator = indicatorRepo.findById(indicator.getId()).orElseThrow(RuntimeException::new);
        if (!DiaryUtils.hasNumericValues(indicator))
            throw new RuntimeException();

        IndMark firstMark = markRepo.findFirstByIndicatorOrderByDateEntityValue(indicator);
        if (firstMark == null) {
            return;
        }

//        System.out.println("***** START *****");
//        System.out.println("First date " + DateUtils.READABLE_DATE_FORMAT.format(firstMark.getDate()));
        IndValue zeroValue = getZeroValue(indicator);
        Date date = firstMark.getDate();
        Date today = new Date();
        while (DateUtils.before(date, today)) {
            if (!markRepo.existsByIndicatorAndDateEntityValue(indicator, date)) {
//                System.out.println("Create " + DateUtils.READABLE_DATE_FORMAT.format(date));
                IndMark mark = new IndMark(
                        indicator,
                        dateService.createIfNotExistAndGetDateEntity(date),
                        zeroValue,
                        null
                );
                markRepo.save(mark);
            }
            date = DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, 1);
        }
//        System.out.println("***** END *****");
    }

    private IndValue getZeroValue(Indicator indicator) {
        for (IndValue value : indicator.getValues()) {
            try {
                int intValue = Integer.parseInt(value.getTitle());
                if (intValue == 0)
                    return value;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        throw new RuntimeException();
    }
}
