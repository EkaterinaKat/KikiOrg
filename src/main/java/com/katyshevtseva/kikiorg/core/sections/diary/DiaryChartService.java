package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.Cache;
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
    private final ZeroMarkCreationService zeroService;
    private final IndMarkRepo markRepo;
    private final Cache<Date, Period> monthPeriodCache = new Cache<>(DateUtils::getPeriodOfMonthDateBelongsTo);
    private final Cache<Date, Period> weekPeriodCache = new Cache<>(DateUtils::getPeriodOfWeekDateBelongsTo);

    public List<Dot> getChart(Indicator indicator, Span span, ChartYValueType yValueType) {
        zeroService.createZeroMarks(indicator, span);
        List<IndMark> marks = markRepo.findByIndicator(indicator);

        Map<Period, List<IndMark>> periodMarkListMap = getPeriodMarkListMapByMarks(marks, getPeriodCache(span));
        List<Map.Entry<Period, List<IndMark>>> entryList = periodMarkListMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().start()))
                //чтобы не показывать ещё не оконченный период
                .filter(entry -> !dateBelongsToPeriod(entry.getKey(), new Date()))
                .collect(Collectors.toList());

        List<Dot> dots = new ArrayList<>();

        for (Map.Entry<Period, List<IndMark>> entry : entryList) {

            Float yValue = getYValueOfDot(entry, yValueType);

            String details = entry.getValue().stream()
                    .sorted(Comparator.comparing(mark -> mark.getDateEntity().getValue()))
                    .map(mark -> String.format("%s = %s",
                            READABLE_DATE_FORMAT.format(mark.getDateEntity().getValue()),
                            mark.getValue().getTitle()))
                    .reduce((s, s2) -> s + "\n" + s2).orElse("-");

            dots.add(new Dot(getPeriodTitle(span, entry.getKey()), yValue, details));
        }

        return dots;
    }

    private Float getYValueOfDot(Map.Entry<Period, List<IndMark>> entry, ChartYValueType yValueType) {
        switch (yValueType) {
            case AVERAGE:
                return (float) entry.getValue().stream()
                        .mapToInt(mark -> Integer.parseInt(mark.getValue().getTitle()))
                        .average()
                        .orElse(0);
            case SUM:
                return (float) entry.getValue().stream()
                        .mapToInt(mark -> Integer.parseInt(mark.getValue().getTitle()))
                        .sum();
        }
        throw new RuntimeException();
    }

    private String getPeriodTitle(Span span, Period period) {
        switch (span) {
            case MONTH:
                return MONTH_YEAR_DATE_FORMAT.format(period.start());
            case WEEK:
                return DAY_MONTH_DATE_FORMAT.format(period.start());
        }
        throw new RuntimeException();
    }

    private Cache<Date, Period> getPeriodCache(Span span) {
        switch (span) {
            case MONTH:
                return monthPeriodCache;
            case WEEK:
                return weekPeriodCache;
        }
        throw new RuntimeException();
    }

    private Map<Period, List<IndMark>> getPeriodMarkListMapByMarks(
            List<IndMark> marks,
            Cache<Date, Period> periodCache) {

        Map<Period, List<IndMark>> periodMarkListMap = new HashMap<>();
        for (IndMark mark : marks) {
            Period period = periodCache.getValue(mark.getDateEntity().getValue());
            List<IndMark> markList = periodMarkListMap.getOrDefault(period, new ArrayList<>());
            markList.add(mark);
            periodMarkListMap.put(period, markList);
        }
        return periodMarkListMap;
    }

    @Data
    @AllArgsConstructor
    public static class Dot {
        private String x;
        private Float y;
        private String details;
    }
}
