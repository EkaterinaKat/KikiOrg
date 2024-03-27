package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
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
public class StudyTableService {
    private static final int columnWidth = 130;
    private final StudyService studyService;

    public List<List<ReportCell>> getReport(Period period) {
        return getReport(studyService.getActiveSubjects(), period);
    }

    public List<List<ReportCell>> getReport(List<Subject> subjects, Period period) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead(subjects));
        List<Date> dates = getDateRange(period);
        Collections.reverse(dates);  // Чтобы последние даты были наверху таблицы
        for (Date date : dates) {
            result.add(getReportLine(date, subjects));
        }
        return result;
    }

    private List<ReportCell> getReportLine(Date date, List<Subject> subjects) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().text(READABLE_DATE_FORMAT.format(date)).width(100).build());
        for (Subject subject : subjects) {
            result.add(convertToCell(subject, date));
        }
        return result;
    }

    private ReportCell convertToCell(Subject subject, Date date) {
        SubjMark mark = studyService.getMark(subject, date).orElse(null);
        if (mark == null) {
            return ReportCell.builder().item(getMarkToEdit(subject, date)).build();
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

    private List<ReportCell> getReportHead(List<Subject> subjects) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().build());
        for (Subject subject : subjects) {
            result.add(ReportCell.builder().type(HEAD_COLUMN).text(subject.getTitle()).width(columnWidth).build());
        }
        return result;
    }

    private MarkToEdit getMarkToEdit(Subject subject, Date date) {
        return new MarkToEdit() {
            @Override
            public Subject getSubject() {
                return subject;
            }

            @Override
            public Date getDate() {
                return date;
            }

            @Override
            public SubjMark getMark() {
                return null;
            }
        };
    }

    public interface MarkToEdit {
        Subject getSubject();

        Date getDate();

        SubjMark getMark();
    }
}
