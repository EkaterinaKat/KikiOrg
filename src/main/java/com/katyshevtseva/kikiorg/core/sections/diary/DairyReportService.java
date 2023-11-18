package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.fx.Styler.StandardColor.WHITE;

@RequiredArgsConstructor
@Service
public class DairyReportService {
    private static final int columnWidth = 130;
    private final DiaryService diaryService;

    public List<List<ReportCell>> getQuickReport() {
        return getReport(diaryService.getIndicators(),
                new Period(shiftDate(new Date(), DateUtils.TimeUnit.DAY, -30), new Date()));
    }

    public List<List<ReportCell>> getReport(List<Indicator> indicators, Period period) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead(indicators));
        List<Date> dates = getDateRange(period);
        Collections.reverse(dates);  // Чтобы последние даты были наверху таблицы
        for (Date date : dates) {
            result.add(getReportLine(date, indicators));
        }
        return result;
    }

    private List<ReportCell> getReportLine(Date date, List<Indicator> indicators) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.filled(READABLE_DATE_FORMAT.format(date), WHITE, 100));
        for (Indicator indicator : indicators) {
            result.add(convertToCell(indicator, date));
        }
        return result;
    }

    private ReportCell convertToCell(Indicator indicator, Date date) {
        IndMark mark = diaryService.getMark(indicator, date).orElse(null);
        if (mark == null) {
            return ReportCell.empty();
        }
        String color = mark.getValue() != null ?
                (!GeneralUtils.isEmpty(mark.getValue().getColor()) ? mark.getValue().getColor() : WHITE.getCode())
                : WHITE.getCode();
        return ReportCell.filled(mark.getValueAndComment(), color, columnWidth);
    }

    private List<ReportCell> getReportHead(List<Indicator> indicators) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.empty());
        for (Indicator indicator : indicators) {
            result.add(ReportCell.columnHead(indicator.getTitle(), columnWidth));
        }
        return result;
    }
}
