package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService.HabitMark;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumMark;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.NumMark;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CheckListController implements FxController {
    private HabitsService habitsService = Core.getInstance().habitsService();
    private HabitMarkService habitMarkService = Core.getInstance().habitMarkService();
    @FXML
    private DatePicker datePicker;
    @FXML
    private HBox hPane;
    @FXML
    private Button saveButton;
    private List<Pair> pairs;
    private List<Habit> habits;

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> save());
        Utils.associateButtonWithControls(saveButton, datePicker);
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(event -> saveButton.setDisable(false));
        fillHabitsTable();
    }

    private void fillHabitsTable() {
        habits = habitsService.getActiveHabits();
        pairs = new ArrayList<>();

        for (HabitGroup habitGroup : HabitGroup.values()) {
            List<Habit> groupHabits = selectHabitsByGroup(habitGroup);
            if (!groupHabits.isEmpty()) {
                VBox vPane = new VBox();
                Label groupLabel = new Label(habitGroup.getName().toUpperCase());
                groupLabel.setStyle(Utils.getBlackTextStyle());
                vPane.getChildren().add(groupLabel);
                Pane pane1 = new Pane();
                pane1.setPrefHeight(15);
                vPane.getChildren().add(pane1);
                GridPane gridPane = new GridPane();
                gridPane.setVgap(15);
                gridPane.setHgap(15);
                int rowIndex = 0;
                for (Habit habit : groupHabits) {
                    Pair pair = new Pair(habit);
                    pairs.add(pair);
                    gridPane.add(pair.getLabel(), 0, rowIndex);
                    gridPane.add(pair.markControl, 1, rowIndex);
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
        for (Pair pair : pairs) {
            habitMarkService.saveMarkOrRewriteIfExists(
                    pair.habit, java.sql.Date.valueOf(datePicker.getValue()), pair.getMarkControlValue());
        }
        saveButton.setDisable(true);
    }

    private class Pair {
        final int NODE_WIDTH = 180;
        Habit habit;
        Control markControl;

        Pair(Habit habit) {
            this.habit = habit;
            HabitMark lastMark = habitMarkService.getLastMarkByHabitWithinLastWeekOrNull(habit);

            switch (habit.getType()) {
                case bollean:
                    CheckBox checkBox = new CheckBox();
                    markControl = checkBox;
                    if (lastMark != null)
                        checkBox.setSelected(true);
                    break;
                case number:
                    TextField textField = new TextField();
                    textField.setMaxWidth(NODE_WIDTH);
                    textField.setMinWidth(NODE_WIDTH);
                    Utils.disableNonNumericChars(textField);
                    markControl = textField;
                    if (lastMark != null) {
                        textField.setText("" + ((NumMark) lastMark).getValue());
                    }
                    break;
                case enumeration:
                    ComboBox<EnumElement> comboBox = new ComboBox<>();
                    comboBox.setItems(FXCollections.observableArrayList(
                            habitsService.getEnumElementsByHabit(habit)));
                    comboBox.setMaxWidth(NODE_WIDTH);
                    comboBox.setMinWidth(NODE_WIDTH);
                    markControl = comboBox;
                    if (lastMark != null) {
                        comboBox.setValue(((EnumMark) lastMark).getEnumElement());
                    }
            }
        }

        Label getLabel() {
            return new Label(habit.getTitle());
        }

        // Возвращает Boolean, Integer EnumElement или null
        Object getMarkControlValue() {
            switch (habit.getType()) {
                case bollean:
                    return ((CheckBox) markControl).isSelected();
                case number:
                    String textFieldValue = ((TextField) markControl).getText();
                    if (textFieldValue == null || textFieldValue.isEmpty())
                        return 0;
                    return Integer.parseInt(textFieldValue);
                case enumeration:
                    return ((ComboBox<EnumElement>) markControl).getValue();
            }
            throw new RuntimeException();
        }
    }
}
