package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.Cache;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjMarkRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.*;

@RequiredArgsConstructor
@Service
public class StudyChartService {
    private final ZeroMarkCreationService2 zeroService;
    private final SubjMarkRepo markRepo;
    private final Cache<Date, Period> monthPeriodCache = new Cache<>(DateUtils::getPeriodOfMonthDateBelongsTo);
    private final Cache<Date, Period> weekPeriodCache = new Cache<>(DateUtils::getPeriodOfWeekDateBelongsTo);

    public List<Dot> getChart(Subject subject, Span span, ChartYValueType yValueType) {
        zeroService.createZeroMarks(subject);
        List<SubjMark> marks = markRepo.findBySubject(subject);

        Map<Period, List<SubjMark>> periodMarkListMap = getPeriodMarkListMapByMarks(marks, getPeriodCache(span));
        List<Map.Entry<Period, List<SubjMark>>> entryList = periodMarkListMap.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().start()))
                //чтобы не показывать ещё не оконченный период
                .filter(entry -> !dateBelongsToPeriod(entry.getKey(), new Date()))
                .collect(Collectors.toList());

        List<Dot> dots = new ArrayList<>();

        for (Map.Entry<Period, List<SubjMark>> entry : entryList) {

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

    private Float getYValueOfDot(Map.Entry<Period, List<SubjMark>> entry, ChartYValueType yValueType) {
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

    private Map<Period, List<SubjMark>> getPeriodMarkListMapByMarks(
            List<SubjMark> marks,
            Cache<Date, Period> periodCache) {

        Map<Period, List<SubjMark>> periodMarkListMap = new HashMap<>();
        for (SubjMark mark : marks) {
            Period period = periodCache.getValue(mark.getDateEntity().getValue());
            List<SubjMark> markList = periodMarkListMap.getOrDefault(period, new ArrayList<>());
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
