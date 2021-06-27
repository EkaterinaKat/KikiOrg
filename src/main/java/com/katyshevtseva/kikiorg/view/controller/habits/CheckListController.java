package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.*;

class CheckListController implements FxController {
    private HabitsService habitsService = Core.getInstance().habitsService();
    private HabitMarkService habitMarkService = Core.getInstance().habitMarkService();
    @FXML
    private DatePicker datePicker;
    @FXML
    private VBox habitsPane;
    @FXML
    private Button saveButton;
    private Map<Habit, CheckBox> habitCheckBoxMap;

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> save());
        associateButtonWithControls(saveButton, datePicker);
        datePicker.setValue(new java.sql.Date(DateCorrector.getProperDate().getTime()).toLocalDate());
        datePicker.setOnAction(event -> saveButton.setDisable(false));
        fillHabitsTable();
    }

    private void fillHabitsTable() {
        List<Habit> habits = habitsService.getActiveHabits();
        habitCheckBoxMap = new HashMap<>();

        for (Habit habit : habits) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            habitCheckBoxMap.put(habit, checkBox);

            HBox hBox = new HBox();
            hBox.getChildren().addAll(checkBox, getPaneWithWidth(20), new Label(habit.getTitle()));
            habitsPane.getChildren().addAll(hBox, getPaneWithHeight(20));

        }
    }

    private void save() {
        for (Map.Entry<Habit, CheckBox> entry : habitCheckBoxMap.entrySet()) {
            habitMarkService.saveMarkOrRewriteIfExists(
                    entry.getKey(), java.sql.Date.valueOf(datePicker.getValue()), entry.getValue().isSelected());
        }
        saveButton.setDisable(true);
    }
}
