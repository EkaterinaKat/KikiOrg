package com.katyshevtseva.kikiorg.core.sections.work;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.kikiorg.core.report.ReportCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.date.DateUtils.getDateRange;
import static com.katyshevtseva.kikiorg.core.report.ReportCell.Color.SLATE_BLUE;

@Service
public class WorkReportService {
    @Autowired
    private WorkService workService;

    public List<List<ReportCell>> getReport(Period period) {
        List<List<ReportCell>> result = new ArrayList<>();
        result.add(getReportHead());
        List<Date> dates = getDateRange(period);
        Collections.reverse(dates);  // Чтобы последние даты были наверху таблицы
        for (Date date : dates) {
            result.add(getReportLine(date));
        }
        return result;
    }

    private List<ReportCell> getReportHead() {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.empty());
        for (WorkArea workArea : WorkArea.values()) {
            result.add(ReportCell.columnHead(workArea.getTitle()));
        }
        result.add(ReportCell.columnHead("Total"));
        return result;
    }

    private List<ReportCell> getReportLine(Date date) {
        List<ReportCell> result = new ArrayList<>();
        result.add(ReportCell.date(READABLE_DATE_FORMAT.format(date)));
        for (WorkArea workArea : WorkArea.values()) {
            result.add(convertToCell(workArea, date));
        }
        result.add(getTotalCellByDate(date));
        return result;
    }

    private ReportCell convertToCell(WorkArea workArea, Date date) {
        WorkLog workLog = workService.getWorkLogOrNull(workArea, date);
        if (workLog == null)
            return ReportCell.empty();
        return ReportCell.filled("" + workLog.getMinutes());
    }

    private ReportCell getTotalCellByDate(Date date) {
        int totalMinutes = 0;
        for (WorkArea workArea : WorkArea.values()) {
            WorkLog workLog = workService.getWorkLogOrNull(workArea, date);
            if (workLog != null)
                totalMinutes += workLog.getMinutes();
        }
        if (totalMinutes > 0)
            return ReportCell.filled("" + totalMinutes, SLATE_BLUE);
        return ReportCell.empty();
    }
}
