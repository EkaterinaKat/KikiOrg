package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.report.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.utils.ReportUtils;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.List;

class ReportTableController implements FxController {
    @FXML
    private GridPane reportTable;

    @FXML
    private void initialize() {

    }

    void showReport(List<Habit> habits, Period period) {
        List<List<ReportCell>> report = Core.getInstance().habitsReportService().getReport(habits, period);
        ReportUtils.showReport(report, reportTable);
    }
}
