package com.katyshevtseva.kikiorg.core.sections.diary;

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

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.date.DateUtils.getDateRange;
import static com.katyshevtseva.fx.Styler.StandardColor.WHITE;
import static com.katyshevtseva.general.ReportCell.Type.HEAD_COLUMN;

@RequiredArgsConstructor
@Service
public class DairyTableService {
    private static final int columnWidth = 130;
    private final DiaryService diaryService;

    public List<List<ReportCell>> getReport(Period period) {
        return getReport(diaryService.getActiveIndicators(), period);
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
        result.add(ReportCell.builder().text(READABLE_DATE_FORMAT.format(date)).width(100).build());
        for (Indicator indicator : indicators) {
            result.add(convertToCell(indicator, date));
        }
        return result;
    }

    private ReportCell convertToCell(Indicator indicator, Date date) {
        IndMark mark = diaryService.getMark(indicator, date).orElse(null);
        if (mark == null) {
            return ReportCell.builder().item(getMarkToEdit(indicator, date)).build();
        }
        String color = mark.getValue() != null ?
                (!GeneralUtils.isEmpty(mark.getValue().getColor()) ? mark.getValue().getColor() : WHITE.getCode())
                : WHITE.getCode();
        return ReportCell.builder()
                .text(mark.getValueAndComment())
                .color(color).width(columnWidth)
                .item(mark)
                .build();
    }

    private List<ReportCell> getReportHead(List<Indicator> indicators) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().build());
        for (Indicator indicator : indicators) {
            result.add(ReportCell.builder().type(HEAD_COLUMN).text(indicator.getTitle()).width(columnWidth).build());
        }
        return result;
    }

    private MarkToEdit getMarkToEdit(Indicator indicator, Date date) {
        return new MarkToEdit() {
            @Override
            public Indicator getIndicator() {
                return indicator;
            }

            @Override
            public Date getDate() {
                return date;
            }

            @Override
            public IndMark getMark() {
                return null;
            }
        };
    }

    public interface MarkToEdit {
        Indicator getIndicator();

        Date getDate();

        IndMark getMark();
    }
}
