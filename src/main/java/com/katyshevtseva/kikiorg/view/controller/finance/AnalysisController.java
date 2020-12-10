package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.Calendar;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

class AnalysisController implements FxController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button showButton;
    @FXML
    private BarChart<String, Long> chart;
    @FXML
    private GridPane table;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showButtonListener());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);
        setInitialDates();
        adjustChart();
    }

    private void adjustChart() {
        chart.setTitle("Income vs Expenses");
    }

    private void setInitialDates() {
        Calendar calendar = Calendar.getInstance();
        startDatePicker.setValue(LocalDate.of(
                calendar.get(YEAR),
                calendar.get(MONTH), // Calendar считает месяцы с 0, а LocalDate с 1, поэтому происходит смещение на 1 месяц
                calendar.get(Calendar.DATE)));
        endDatePicker.setValue(LocalDate.now());
    }

    private void showButtonListener() {
        chart.getData().clear();
        table.getChildren().clear();

        long incomeAmount = Core.getInstance().incomeReportService().getIncomeReport(
                Utils.getPeriodByDp(startDatePicker, endDatePicker)).getTotal();
        long expensesAmount = Core.getInstance().expensesReportService().getHeadReport(
                Utils.getPeriodByDp(startDatePicker, endDatePicker)).getTotal();

        XYChart.Series<String, Long> series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<String, Long>("", incomeAmount));
        series1.setName("Income " + incomeAmount);
        chart.getData().add(series1);

        XYChart.Series<String, Long> series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<String, Long>("", expensesAmount));
        series2.setName("Expenses " + expensesAmount);
        chart.getData().add(series2);

        table.add(new Label("Diff: " + Math.abs(incomeAmount - expensesAmount)), 0, 0);
    }
}
