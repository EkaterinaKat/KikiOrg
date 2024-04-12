package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.general.ReportCell.ReportCellBuilder;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Circs;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjMarkRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.general.ReportCell.Type.HEAD_COLUMN;
import static com.katyshevtseva.kikiorg.core.CoreConstants.STUDY_START_DATE;
import static com.katyshevtseva.time.TimeUtil.getTimeStringByMinutes;

@RequiredArgsConstructor
@Service
public class StudyTableService {
    private static final int COLUMN_WIDTH = 70;
    private static final int CIRCS_COLUMN_WIDTH = 120;
    private static final int DATE_COLUMN_WIDTH = 100;
    private final StudyService studyService;
    private final CircsService circsService;
    private final SubjMarkRepo markRepo;
    private final PmService pmService;

    public List<List<ReportCell>> getReport(Period period) {
        return getReport(studyService.getActiveSubjects(), period);
    }

    public List<List<ReportCell>> getReport(List<Subject> subjects, Period period) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead(subjects));
        List<Date> dates = getDateRange(period);
        Collections.reverse(dates);  // Чтобы последние даты были наверху таблицы
        Date today = new Date();
        for (Date date : dates) {
            result.add(getReportLine(date, subjects, today));
        }
        return result;
    }

    private List<ReportCell> getReportLine(Date date, List<Subject> subjects, Date today) {
        List<ReportCell> result = new ArrayList<>();
        result.add(getDateCell(date, today));
        result.add(getCircsCell(date));
        for (Subject subject : subjects) {
            result.add(getSubjMarkCell(subject, date));
        }
        return result;
    }

    private ReportCell getDateCell(Date date, Date today) {
        ReportCellBuilder builder = ReportCell.builder()
                .text(READABLE_DATE_FORMAT.format(date))
                .width(DATE_COLUMN_WIDTH);
        if (DateUtils.equalsIgnoreTime(date, today)) {
            builder.color(Styler.StandardColor.GOLD.getCode());
        }
        return builder.build();
    }

    private ReportCell getCircsCell(Date date) {
        Circs circs = circsService.getCircsOrNull(date);
        if (circs == null) {
            ReportCellBuilder builder = ReportCell.builder().item(getNonexistentCircsToEdit(date));
            Date today = new Date();
            if (circsCellShouldBeFilled(date, today))
                builder.color(Styler.StandardColor.RED.getCode());
            return builder.build();
        } else
            return (ReportCell.builder().item(circs).text(circs.getInfo()).width(CIRCS_COLUMN_WIDTH).build());
    }

    private boolean circsCellShouldBeFilled(Date date, Date today) {
        if (before(date, STUDY_START_DATE) || !before(date, today)) {
            return false;
        }
        return markRepo.findByDateEntityValue(date).stream().mapToInt(SubjMark::getMinutes).sum() == 0;
    }

    private ReportCell getSubjMarkCell(Subject subject, Date date) {
        SubjMark mark = studyService.getMark(subject, date).orElse(null);
        String cellColor = pmService.markExists(subject, date) ? BLUE.getCode() : WHITE.getCode();

        if (mark == null) {
            return ReportCell.builder()
                    .item(getNonexistentMarkToEdit(subject, date))
                    .color(cellColor)
                    .width(COLUMN_WIDTH)
                    .build();
        }
        return ReportCell.builder()
                .text(getTimeStringByMinutes(mark.getMinutes(), true))
                .textColor(mark.getMinutes() == 0 ? LIGHT_GREY.getCode() : BLACK.getCode())
                .color(cellColor)
                .width(COLUMN_WIDTH)
                .item(mark)
                .build();
    }

    private List<ReportCell> getReportHead(List<Subject> subjects) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.builder().build());//пустая клетка над колонкой дат 
        result.add(ReportCell.builder().build());//пустая клетка над колонкой circs
        for (Subject subject : subjects) {
            result.add(ReportCell.builder().type(HEAD_COLUMN).text(subject.getTitle()).build());
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
