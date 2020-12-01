package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.Calendar;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

class ReportController implements FxController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button showButton;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showReport());
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

    private void showReport() {

    }
}
