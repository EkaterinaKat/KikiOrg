package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitType;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitsManager;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

class AdminSubmodeController implements FxController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button newHabitButton;
    @FXML
    private Pane showPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Button editButton;
    @FXML
    private Pane editPane;
    @FXML
    private TextField titleTextField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private ComboBox<HabitType> typeComboBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;

    enum Mode {show, edit}

    @FXML
    private void initialize() {
        fillHabitTable();
        setTypeComboBoxItems();
        newHabitButton.setOnAction(event -> createHabit());
        editButton.setOnAction(event -> edit());
        saveButton.setOnAction(event -> save());
        Utils.associateButtonWithControls(saveButton, titleTextField, typeComboBox, descTextArea);
    }

    private void setTypeComboBoxItems() {
        ObservableList<HabitType> items = FXCollections.observableArrayList(Arrays.asList(HabitType.values()));
        typeComboBox.setItems(items);
    }

    private void fillHabitTable() {
        List<Habit> habits = HabitsManager.getInstance().getAllHabits();
        int rowIndex = 0;
        for(Habit habit: habits){
            Label label = new Label(String.format("%s (active = %s)", habit.getTitle(), habit.isActive()));
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> switchMode(Mode.show, habit));
            gridPane.add(label, 0, rowIndex);
            rowIndex++;
        }
    }

    private void createHabit() {

    }

    private void edit() {

    }

    private void save() {

    }

    private void show(Habit habit) {

    }

    private void switchMode(Mode mode, Habit habit) {
        showPane.setManaged(mode == Mode.show);
        showPane.setVisible(mode == Mode.show);
        editPane.setManaged(mode == Mode.edit);
        editPane.setVisible(mode == Mode.edit);

        if (habit != null && mode == Mode.show) {
            titleLabel.setText(String.format("%s (active = %s)", habit.getTitle(), habit.isActive()));
            descLabel.setText(habit.getDesc());
            typeLabel.setText("type: " + habit.getType());
        } else if(habit != null && mode == Mode.edit) {
            titleTextField.setText(habit.getTitle());
            descTextArea.setText(habit.getDesc());
            activeCheckBox.setSelected(habit.isActive());
            typeComboBox.setValue(habit.getType());
            typeComboBox.setDisable(true);
        } else if(habit == null && mode == Mode.show) {
            titleLabel.setText("");
            descLabel.setText("");
            typeLabel.setText("");
        } else if(habit == null && mode == Mode.edit) {
            titleTextField.clear();
            descTextArea.clear();
            activeCheckBox.setSelected(true);
            typeComboBox.setValue(HabitType.bollean);
            typeComboBox.setDisable(false);
        }
    }
}
