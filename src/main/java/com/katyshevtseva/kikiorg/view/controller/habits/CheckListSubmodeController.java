package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.HabitsManager;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CheckListSubmodeController implements FxController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane habitsTable;
    @FXML
    private Button saveButton;
    private List<Pair> pairs;

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> save());
        Utils.associateButtonWithControls(saveButton, datePicker);
        datePicker.setValue(LocalDate.now());
        fillHabitsTable();
    }

    private void fillHabitsTable() {
        List<Habit> habits = HabitsManager.getInstance().getActiveHabits();
        pairs = new ArrayList<>();
        int index = 0;
        for (Habit habit : habits) {
            int row = index / 3;
            int labelColumn = 0;
            int markNodeColumn = 1;
            if (index % 3 == 1) {
                labelColumn = 2;
                markNodeColumn = 3;
            } else if (index % 3 == 2) {
                labelColumn = 4;
                markNodeColumn = 5;
            }
            index++;

            Pair pair = new Pair(habit);
            pairs.add(pair);
            habitsTable.add(pair.getLabel(), labelColumn, row);
            habitsTable.add(pair.markNode, markNodeColumn, row);
        }
    }

    private void save() {
        for (Pair pair : pairs) {
            HabitsManager.getInstance().makeMark(pair.habit, java.sql.Date.valueOf(datePicker.getValue()), pair.getMark());
        }
    }

    private class Pair {
        final int NODE_WIDTH = 180;
        Habit habit;
        Node markNode;

        Pair(Habit habit) {
            this.habit = habit;

            switch (habit.getType()) {
                case bollean:
                    markNode = new CheckBox();
                    break;
                case number:
                    TextField textField = new TextField();
                    textField.setMaxWidth(NODE_WIDTH);
                    textField.setMinWidth(NODE_WIDTH);
                    Utils.disableNonNumericChars(textField);
                    markNode = textField;
                    break;
                case enumeration:
                    ComboBox<EnumElement> comboBox = new ComboBox<>();
                    comboBox.setItems(FXCollections.observableArrayList(
                            HabitsManager.getInstance().getEnumElementsByHabit(habit)));
                    comboBox.setMaxWidth(NODE_WIDTH);
                    comboBox.setMinWidth(NODE_WIDTH);
                    markNode = comboBox;
            }
        }

        Label getLabel() {
            return new Label(habit.getTitle());
        }

        Object getMark() {
            switch (habit.getType()) {
                case bollean:
                    return ((CheckBox) markNode).isSelected();
                case number:
                    return Long.parseLong(((TextField) markNode).getText());
                case enumeration:
                    return ((ComboBox<EnumElement>) markNode).getValue();
            }
            throw new RuntimeException();
        }
    }
}
