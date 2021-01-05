package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

class AnalysisController implements FxController {
    @FXML
    private ComboBox habitComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox graduationComboBox;
    @FXML
    private Button showButton;
    @FXML
    private AreaChart<String, Long> chart;
    @FXML
    private GridPane gridPane;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showButtonListener());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker, habitComboBox, graduationComboBox);
        setInitialDates();
    }

    private void setInitialDates() {
        startDatePicker.setValue(new java.sql.Date(DateUtils.getMonthAgoDate().getTime()).toLocalDate());
        endDatePicker.setValue(LocalDate.now());
    }

    private void showButtonListener() {

    }
}
