package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitsManager;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;

class CheckListSubmodeController implements FxController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane habitsTable;
    @FXML
    private Button saveButton;
    @FXML
    private GridPane resultsTable;

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> save());
        Utils.associateButtonWithControls(saveButton, datePicker);
        datePicker.setValue(LocalDate.now());
        fillHabitsTable();
        updateResultsTable();
    }

    private void fillHabitsTable() {
        List<Habit> habits = HabitsManager.getInstance().getActiveHabits();
        int index = 0;
        habits.forEach(habit -> {

        });
    }

    private void updateResultsTable() {

    }

    private void save() {

        updateResultsTable();
    }

}
