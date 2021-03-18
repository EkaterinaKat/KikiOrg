package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ExpensesReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.IncomeReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.Report;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportSegment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.kikiorg.core.CoreConstants.FINANCIAL_ACCOUNTING_START_DATE;
import static com.katyshevtseva.kikiorg.view.controller.finance.ReportController.Mode.Expenses;
import static com.katyshevtseva.kikiorg.view.controller.finance.ReportController.Mode.Income;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.getPeriodByDp;

class ReportController implements FxController {
    private ExpensesReportService expensesReportService = Core.getInstance().expensesReportService();
    private IncomeReportService incomeReportService = Core.getInstance().incomeReportService();
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
    private ComboBox<Mode> modeComboBox;
    @FXML
    private Button startDateButton;

    enum Mode {
        Income, Expenses
    }

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showButtonListener());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);
        setInitialDates();
        modeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(Income, Expenses)));
        modeComboBox.setValue(Expenses);
        startDateButton.setOnAction(event ->
                startDatePicker.setValue(new java.sql.Date(FINANCIAL_ACCOUNTING_START_DATE.getTime()).toLocalDate()));
    }

    private void setInitialDates() {
        startDatePicker.setValue(new java.sql.Date(DateUtils.getMonthAgoDate().getTime()).toLocalDate());
        endDatePicker.setValue(LocalDate.now());
    }

    private void showButtonListener() {
        if (modeComboBox.getValue() == Expenses)
            showReport(expensesReportService.getHeadReport(getPeriodByDp(startDatePicker, endDatePicker)));
        if (modeComboBox.getValue() == Income)
            showReport(incomeReportService.getIncomeReport(getPeriodByDp(startDatePicker, endDatePicker)));
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
                            showReport(segment.getChildReport(getPeriodByDp(startDatePicker, endDatePicker)));
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
