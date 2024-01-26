package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Month;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.LabelBuilder;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.general.HasDate;
import com.katyshevtseva.general.OneOutKnob;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.fx.Styler.StandardColor.GREEN;
import static com.katyshevtseva.fx.Styler.StandardColor.LIGHT_GREY;


//PMV - period marks visualisation
public class YearVisualisationUtil {

    public static Map<String, List<Mark>> splitIntoYears(List<Mark> marks) {
        Map<String, List<Mark>> map = new HashMap<>();
        for (Mark mark : marks) {
            String year = YEAR_FORMAT.format(mark.getDate());
            List<Mark> marks1 = map.getOrDefault(year, new ArrayList<>());
            marks1.add(mark);
            map.put(year, marks1);
        }
        return map;
    }

    public static Map<Month, List<Mark>> splitIntoMonths(List<Mark> marks) {
        Map<Month, List<Mark>> map = new HashMap<>();
        for (Mark mark : marks) {
            Month month = Month.findByDate(mark.getDate());
            List<Mark> marks1 = map.getOrDefault(month, new ArrayList<>());
            marks1.add(mark);
            map.put(month, marks1);
        }
        return map;
    }

    public static Node getMonthVisualisation(List<Mark> marks, Period period) {
        HBox box = new HBox();
        for (Date date : DateUtils.getDateRange(period)) {
            Mark mark = marks.stream().filter(m -> m.getDate().equals(date)).findFirst().orElse(null);
            if (mark == null) {
                box.getChildren().add(new LabelBuilder().text("■").color(LIGHT_GREY).build());
            } else {
                box.getChildren().add(new LabelBuilder().text("■").color(GREEN).build());
            }
        }
        return box;
    }

    public static String getSummary(List<Mark> marks, Period period) {
        if (GeneralUtils.isEmpty(marks)) {
            return "-";
        }
        int numOfDays = DateUtils.getNumberOfDaysInclBounds(period);
        int percents = (marks.size() * 100) / numOfDays;
        return String.format("%d дней из %d => %d", marks.size(), numOfDays, percents) + "%";
    }

    public static void validateMarksAndPeriod(List<Mark> marks, Period period) {
        for (Mark mark : marks) {
            if (!dateBelongsToPeriod(period, mark.getDate())) {
                throw new RuntimeException();
            }
        }
        Set<Date> dateSet = marks.stream().map(Mark::getDate).collect(Collectors.toSet());
        if (dateSet.size() < marks.size()) {
            throw new RuntimeException();
        }
    }


    public static Period getPeriodOfMonth(String year, Month month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(YEAR_FORMAT.parse(year));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.set(Calendar.MONTH, month.getIndex());
        return getPeriodOfMonthDateBelongsTo(calendar.getTime());
    }

    public enum PeriodType {
        YEAR
    }

    @AllArgsConstructor
    @Getter
    public static class PmvData<T extends HasDate> {
        private PeriodType periodType;
        private List<PmvItem<T>> items;
        private String summary;
        private OneOutKnob<Node> emptyNodeSupplier;
    }

    @AllArgsConstructor
    @Getter
    public static class PmvItem<T extends HasDate> {
        private T item;
        private Node node;
    }
}
