package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.Styler.StandardColor.BLACK;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;

class CheckListController implements FxController {
    private HabitsService habitsService = Core.getInstance().habitsService();
    private HabitMarkService habitMarkService = Core.getInstance().habitMarkService();
    @FXML
    private DatePicker datePicker;
    @FXML
    private HBox hPane;
    @FXML
    private Button saveButton;
    private List<Habit> habits;
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
        habits = habitsService.getActiveHabits();
        habitCheckBoxMap = new HashMap<>();

        for (HabitGroup habitGroup : HabitGroup.values()) {
            List<Habit> groupHabits = selectHabitsByGroup(habitGroup);
            if (!groupHabits.isEmpty()) {
                VBox vPane = new VBox();
                Label groupLabel = new Label(habitGroup.getName().toUpperCase());
                groupLabel.setStyle(Styler.getColorfullStyle(TEXT, BLACK));
                vPane.getChildren().add(groupLabel);
                Pane pane1 = new Pane();
                pane1.setPrefHeight(15);
                vPane.getChildren().add(pane1);
                GridPane gridPane = new GridPane();
                gridPane.setVgap(15);
                gridPane.setHgap(15);
                int rowIndex = 0;
                for (Habit habit : groupHabits) {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(true);
                    habitCheckBoxMap.put(habit, checkBox);
                    gridPane.add(new Label(habit.getTitle()), 0, rowIndex);
                    gridPane.add(checkBox, 1, rowIndex);
                    rowIndex++;
                }
                vPane.getChildren().add(gridPane);
                vPane.getChildren().add(new Label(""));
                hPane.getChildren().add(vPane);
                Pane pane = new Pane();
                pane.setPrefWidth(30);
                hPane.getChildren().add(pane);
            }
        }
    }

    private List<Habit> selectHabitsByGroup(HabitGroup habitGroup) {
        List<Habit> selectedHabits = new ArrayList<>();
        for (Habit habit : habits)
            if (habit.getHabitGroup() == habitGroup)
                selectedHabits.add(habit);
        return selectedHabits;
    }

    private void save() {
        for (Map.Entry<Habit, CheckBox> entry : habitCheckBoxMap.entrySet()) {
            habitMarkService.saveMarkOrRewriteIfExists(
                    entry.getKey(), java.sql.Date.valueOf(datePicker.getValue()), entry.getValue().isSelected());
        }
        saveButton.setDisable(true);
    }
}
