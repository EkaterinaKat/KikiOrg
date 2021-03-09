package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Date;

import static com.katyshevtseva.date.Utils.TimeUnit.DAY;
import static com.katyshevtseva.date.Utils.TimeUnit.MONTH;
import static com.katyshevtseva.date.Utils.shiftDate;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;
import static com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus.STABILITY_LOST;
import static com.katyshevtseva.kikiorg.core.sections.habits.StabilityStatus.STABLE;

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
    private Button stabilityButton;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showButtonListener());
        stabilityButton.setOnAction(event -> stabilityButtonListener());
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
            String analysisResult = Core.getInstance().analysisService().simpleAnalyze(
                    habit, Utils.getPeriodByDp(startDatePicker, endDatePicker));
            resultsBox.getChildren().add(new Label(analysisResult));
        }
    }

    private void stabilityButtonListener() {
        Date yesterday = shiftDate(new Date(), DAY, -1);
        Date threeMonthAgo = shiftDate(yesterday, MONTH, -3);
        startDatePicker.setValue(new java.sql.Date(threeMonthAgo.getTime()).toLocalDate());
        endDatePicker.setValue(new java.sql.Date(yesterday.getTime()).toLocalDate());

        resultsBox.getChildren().clear();
        for (Habit habit : Core.getInstance().habitsService().getActiveHabits()) {
            Label label = new Label(Core.getInstance().analysisService().analyzeStabilityAndAssignNewStatusIfNeeded(habit));
            if (habit.getStabilityStatus() == STABLE)
                label.setStyle(getColorfullStyle(TEXT, "#006400"));
            if (habit.getStabilityStatus() == STABILITY_LOST)
                label.setStyle(getColorfullStyle(TEXT, "#800000"));
            resultsBox.getChildren().add(label);
        }
    }
}
