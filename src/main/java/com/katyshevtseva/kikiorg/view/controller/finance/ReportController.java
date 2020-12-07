package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.Report;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportSegment;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

class ReportController implements FxController {
    private FinanceReportService reportService = Core.getInstance().financeReportService();
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button showButton;
    @FXML
    private PieChart chart;
    @FXML
    private GridPane table;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showRootReport());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);
        setInitialDates();
    }

    private void setInitialDates() {
        Calendar calendar = Calendar.getInstance();
        startDatePicker.setValue(LocalDate.of(
                calendar.get(YEAR),
                calendar.get(MONTH), // Calendar считает месяцы с 0, а LocalDate с 1, поэтому происходит смещение на 1 месяц
                calendar.get(Calendar.DATE)));
        endDatePicker.setValue(LocalDate.now());
    }

    private void showRootReport() {
        showReport(reportService.getHeadReport(
                Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue())));
    }

    private void showReport(Report report) {
        showChart(report);
        showTable(report);
    }

    private void showChart(Report report) {
        ObservableList<Data> chartData = FXCollections.observableArrayList();
        Map<Data, ReportSegment> chartDataAndNodeMapping = new HashMap<>();
        for (ReportSegment segment : report.getSegments()) {
            Data data = new Data(segment.getTitle(), segment.getPercent());
            chartData.add(data);
            chartDataAndNodeMapping.put(data, segment);
        }

        chart.setData(chartData);
        chart.setTitle(report.getTitle());

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        ReportSegment segment = chartDataAndNodeMapping.get(data);
                        if (segment.hasChildren()) {
                            showReport(segment.getChildReport(
                                    Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue())));
                        }
                    });
        }
    }

    private void showTable(Report report) {
        table.getChildren().clear();
        int titleColumn = 0;
        int amountColumn = 1;
        int percentColumn = 2;

        table.add(new Label("Total:"), titleColumn, 0);
        table.add(new Label("   " + report.getTotal()), amountColumn, 0);

        List<ReportSegment> segments = report.getSegments();
        for (int i = 0; i < segments.size(); i++) {
            Label titleLabel = new Label(segments.get(i).getTitle());
            titleLabel.setMaxWidth(140);
            titleLabel.setWrapText(true);
            table.add(titleLabel, titleColumn, i + 1);
            table.add(new Label("   " + segments.get(i).getAmount()), amountColumn, i + 1);
            table.add(new Label("   " + segments.get(i).getPercent() + "%"), percentColumn, i + 1);
        }
    }
}
