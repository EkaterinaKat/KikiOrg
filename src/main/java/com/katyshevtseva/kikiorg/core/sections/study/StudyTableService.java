package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Circs;
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
import static com.katyshevtseva.fx.Styler.StandardColor.BLACK;
import static com.katyshevtseva.fx.Styler.StandardColor.LIGHT_GREY;
import static com.katyshevtseva.general.ReportCell.Type.HEAD_COLUMN;

@RequiredArgsConstructor
@Service
public class StudyTableService {
    private static final int columnWidth = 130;
    private final StudyService studyService;
    private final CircsService circsService;

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
        result.add(getCircsCell(date));
        for (Subject subject : subjects) {
            result.add(getSubjMarkCell(subject, date));
        }
        return result;
    }

    private ReportCell getCircsCell(Date date) {
        Circs circs = circsService.getCircsOrNull(date);
        if (circs == null)
            return ReportCell.builder().item(getNonexistentCircsToEdit(date)).build();
        else
            return (ReportCell.builder().item(circs).text(circs.getInfo()).width(150).build());
    }

    private ReportCell getSubjMarkCell(Subject subject, Date date) {
        SubjMark mark = studyService.getMark(subject, date).orElse(null);
        if (mark == null) {
            return ReportCell.builder().item(getNonexistentMarkToEdit(subject, date)).build();
        }
        return ReportCell.builder()
                .text(getHoursMinutesStringByMinutes(mark.getMinutes()))
                .textColor(mark.getMinutes() == 0 ? LIGHT_GREY.getCode() : BLACK.getCode())
                .width(columnWidth)
                .item(mark)
                .build();
    }

    public String getHoursMinutesStringByMinutes(int min) {
        int hours = min / 60;
        int leftMin = min % 60;
        if (leftMin == 0)
            return hours + "";
        return String.format("%d:%02d", hours, leftMin);
    }

    private List<ReportCell> getReportHead(List<Subject> subjects) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().build());//пустая клетка над колонкой дат 
        result.add(ReportCell.builder().build());//пустая клетка над колонкой circs
        for (Subject subject : subjects) {
            result.add(ReportCell.builder().type(HEAD_COLUMN).text(subject.getTitle()).width(columnWidth).build());
        }
        return result;
    }

    private MarkToEdit getNonexistentMarkToEdit(Subject subject, Date date) {
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

    private CircsToEdit getNonexistentCircsToEdit(Date date) {
        return new CircsToEdit() {
            @Override
            public Date getDate() {
                return date;
            }

            @Override
            public Circs getCircs() {
                return null;
            }
        };
    }

    public interface CircsToEdit {
        Date getDate();

        Circs getCircs();
    }
}
