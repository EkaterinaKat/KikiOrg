package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitType;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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
    private TextArea dexcTextArea;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        fillHabitTable();
        setTypeComboBoxItems();
        newHabitButton.setOnAction(event -> createHabit());
        editButton.setOnAction(event -> edit());
        saveButton.setOnAction(event -> save());
        Utils.associateButtonWithControls(saveButton, titleTextField, typeComboBox, dexcTextArea);

    }

    private void setTypeComboBoxItems() {

    }

    private void fillHabitTable() {

    }

    private void createHabit() {

    }

    private void edit() {

    }

    private void save() {

    }

    private void show (Habit habit) {

    }
}
