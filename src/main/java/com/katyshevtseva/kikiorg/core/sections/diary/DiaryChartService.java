package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.Cache;
import com.katyshevtseva.general.OneInOneOutKnob;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import com.katyshevtseva.kikiorg.core.sections.diary.repo.IndMarkRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.*;

@RequiredArgsConstructor
@Service
public class DiaryChartService {
    private final IndMarkRepo markRepo;
    private final Cache<Date, Period> monthPeriodCache = new Cache<>(DateUtils::getPeriodOfMonthDateBelongsTo);
    private final Cache<Date, Period> weekPeriodCache = new Cache<>(DateUtils::getPeriodOfWeekDateBelongsTo);

    public List<Dot> getChart(Indicator indicator, Span span) {
        List<IndMark> marks = markRepo.findByIndicator(indicator);

        switch (span) {
            case MONTH:
                return getChart(marks, monthPeriodCache, period -> MONTH_YEAR_DATE_FORMAT.format(period.start()));
            case WEEK:
                return getChart(marks, weekPeriodCache, period -> DAY_MONTH_DATE_FORMAT.format(period.start()));
        }
        throw new RuntimeException();
    }

    private List<Dot> getChart(List<IndMark> marks,
                               Cache<Date, Period> datePeriodCache,
                               OneInOneOutKnob<Period, String> periodTitleSupplier) {
        Map<Period, List<IndMark>> periodMarkListMap = new HashMap<>();

        for (IndMark mark : marks) {
            Period period = datePeriodCache.getValue(mark.getDateEntity().getValue());
            List<IndMark> markList = periodMarkListMap.getOrDefault(period, new ArrayList<>());
            markList.add(mark);
            periodMarkListMap.put(period, markList);
        }

        List<Dot> dots = new ArrayList<>();

        for (Map.Entry<Period, List<IndMark>> entry : periodMarkListMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().start())).collect(Collectors.toList())) {
            Float average = (float) entry.getValue().stream()
                    .mapToInt(mark -> Integer.parseInt(mark.getValue().getTitle())).average()
                    .orElse(0);

            String details = entry.getValue().stream()
                    .sorted(Comparator.comparing(mark -> mark.getDateEntity().getValue()))
                    .map(mark -> String.format("%s = %s",
                            READABLE_DATE_FORMAT.format(mark.getDateEntity().getValue()),
                            mark.getValue().getTitle()))
                    .reduce((s, s2) -> s + "\n" + s2).orElse("-");

            dots.add(new Dot(periodTitleSupplier.execute(entry.getKey()), average, details));
        }

        return dots;
    }

    @Data
    @AllArgsConstructor
    public static class Dot {
        private String x;
        private Float y;
        private String details;
    }
}
