package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService.AnalysisResult;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

class AnalysisController implements FxController {
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button showButton;
    @FXML
    private VBox resultsBox;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showButtonListener());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);
        setInitialDates();
    }

    private void setInitialDates() {
        startDatePicker.setValue(new java.sql.Date(DateUtils.getMonthAgoDate().getTime()).toLocalDate());
        endDatePicker.setValue(LocalDate.now());
    }

    private void showButtonListener() {
        resultsBox.getChildren().clear();
        for (Habit habit : Core.getInstance().habitsService().getActiveHabits()) {
            AnalysisResult analysisResult = Core.getInstance().analysisService().analyzeHabit(
                    habit, Utils.getPeriodByDp(startDatePicker, endDatePicker));
            resultsBox.getChildren().add(new Label(analysisResult.getSummary()));
        }
    }
}
