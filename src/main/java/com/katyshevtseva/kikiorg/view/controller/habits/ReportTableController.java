package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.ReportCell;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.List;

class ReportTableController implements FxController {
    @FXML
    private GridPane reportTable;

    void showReport(List<List<ReportCell>> report) {
        ReportUtils.showReport(report, reportTable, true);
    }
}
