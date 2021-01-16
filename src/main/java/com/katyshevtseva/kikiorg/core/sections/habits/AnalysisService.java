package com.katyshevtseva.kikiorg.core.sections.habits;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.Period;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService.HabitMark;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AnalysisService {
    @Autowired
    private HabitMarkService markService;

    public AnalysisResult analyze(Habit habit, Period period, ChartGraduation chartGraduation) {
        return new AnalysisResult(getChartData(habit, period, chartGraduation), getStringData(habit, period));
    }

    private List<ChartPoint> getChartData(Habit habit, Period fullPeriod, ChartGraduation chartGraduation) {
        List<Period> periods = splitPeriod(fullPeriod, chartGraduation);
        List<ChartPoint> chartData = new ArrayList<>();
        for (Period period : periods) {
            chartData.add(getChartPoint(habit, period));
        }
        return chartData;
    }

    private ChartPoint getChartPoint(Habit habit, Period period) {
        int numEquivalentsSum = getNumEquivalentSum(habit, period);
        String title = String.format("%s (%d)", DateUtils.getStringExpression(period), numEquivalentsSum);
        return new ChartPoint(title, numEquivalentsSum);
    }

    private int getNumEquivalentSum(Habit habit, Period period) {
        List<HabitMark> marks = markService.getMarkListByPeriod(habit, period);
        int numEquivalentsSum = 0;
        for (HabitMark mark : marks) {
            numEquivalentsSum += mark.getNumEquivalent();
        }
        return numEquivalentsSum;
    }

    private List<Period> splitPeriod(Period period, ChartGraduation chartGraduation) {
        Date boundDate = period.start();
        List<Period> periods = new ArrayList<>();
        while (true) {
            Date periodStart = boundDate;
            Date periodEnd = increaseDate(boundDate, chartGraduation);
            if (periodEnd.after(period.end()))
                break;
            periods.add(new Period(periodStart, periodEnd));
            boundDate = DateUtils.addOneDay(periodEnd);
        }
        return periods;
    }

    private Date increaseDate(Date date, ChartGraduation chartGraduation) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (chartGraduation) {
            case DAY:
                calendar.add(Calendar.DATE, 1);
            case WEEK:
                calendar.add(Calendar.DATE, 7);
            case MONTH:
                calendar.add(Calendar.MONTH, 1);
            case YEAR:
                calendar.add(Calendar.YEAR, 1);
        }
        return calendar.getTime();
    }

    private Map<String, String> getStringData(Habit habit, Period period) {
        Map<String, String> stringData = new HashMap<>();
        stringData.put("Всего отмеченных дней", "+");
        stringData.put("Всего дней в запрашиваемом периоде", "+");
        stringData.put("Процент отмеченных дней в запрашиваемом периоде", "+");
        stringData.put("Наибольшее количество отмеченных дней подряд", "+");
        stringData.put("Всего в числовом эквиваленте", "" + getNumEquivalentSum(habit, period));
        stringData.put("Среднее значение ЧЭ в день", "+");
        stringData.put("Среднее значение ЧЭ в неделю", "+");
        stringData.put("Среднее значение ЧЭ в месяц", "+");
        stringData.put("Среднее значение ЧЭ в год", "+");
        return stringData;
    }

    public enum ChartGraduation {
        DAY, WEEK, MONTH, YEAR
    }

    public class AnalysisResult {
        List<ChartPoint> chartData;
        Map<String, String> stringData;

        AnalysisResult(List<ChartPoint> chartData, Map<String, String> stringData) {
            this.chartData = chartData;
            this.stringData = stringData;
        }

        public List<ChartPoint> getChartData() {
            return chartData;
        }

        public Map<String, String> getStringData() {
            return stringData;
        }
    }

    public class ChartPoint {
        String title;
        int numExp;

        ChartPoint(String title, int numExp) {
            this.title = title;
            this.numExp = numExp;
        }

        public String getTitle() {
            return title;
        }

        public int getNumExp() {
            return numExp;
        }
    }
}
