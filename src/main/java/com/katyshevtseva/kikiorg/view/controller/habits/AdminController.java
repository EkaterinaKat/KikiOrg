package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitType;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AdminController implements FxController {
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
    @FXML
    private TextField enumTextField;
    @FXML
    private Button addEnumButton;
    @FXML
    private Label enumLabel;
    @FXML
    private ComboBox<HabitGroup> groupComboBox;
    private List<EnumElement> enumElements = new ArrayList<>();
    private List<Label> listPoints;

    enum Mode {show, edit}

    @FXML
    private void initialize() {
        fillHabitTable();
        setComboBoxesItems();
        newHabitButton.setOnAction(event -> createHabit());
        Utils.associateButtonWithControls(saveButton, titleTextField, typeComboBox, descTextArea, groupComboBox);
        switchMode(Mode.show, null);
        tuneEnumControls();
        Utils.associateButtonWithControls(addEnumButton, enumTextField);
        addEnumButton.setOnAction(event -> addEnumElement());
    }

    private void tuneEnumControls() {
        addEnumButton.setDisable(true);
        enumTextField.setDisable(true);
        typeComboBox.setOnAction(event -> {
            boolean isEnum = typeComboBox.getValue() == HabitType.enumeration;
            addEnumButton.setDisable(!isEnum);
            enumTextField.setDisable(!isEnum);
            if (!isEnum) {
                enumElements = new ArrayList<>();
                enumLabel.setText("");
            }
        });
    }

    private void addEnumElement() {
        EnumElement enumElement = new EnumElement();
        enumElement.setTitle(enumTextField.getText().trim());
        enumElements.add(enumElement);
        enumTextField.clear();
        enumLabel.setText(Habit.getEnumString(enumElements));
    }

    private void setComboBoxesItems() {
        ObservableList<HabitType> items = FXCollections.observableArrayList(Arrays.asList(HabitType.values()));
        typeComboBox.setItems(items);
        ObservableList<HabitGroup> groups = FXCollections.observableArrayList(Arrays.asList(HabitGroup.values()));
        groupComboBox.setItems(groups);
    }

    private void fillHabitTable() {
        gridPane.getChildren().clear();
        List<Habit> habits = Core.getInstance().habitsService().getAllHabits();
        listPoints = new ArrayList<>();
        int rowIndex = 0;
        for (Habit habit : habits) {
            Label label = new Label(habit.getTitle());
            if (habit.isActive())
                label.setStyle(Utils.getGreenTextStyle());
            else
                label.setStyle(Utils.getGrayTextStyle());
            Label point = new Label();
            listPoints.add(point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                switchMode(Mode.show, habit);
                listPoints.forEach(label1 -> label1.setText(""));
                point.setText("*");
                editButton.setOnAction(event1 -> switchMode(Mode.edit, habit));
            });
            gridPane.add(point, 0, rowIndex);
            gridPane.add(label, 1, rowIndex);
            rowIndex++;
        }
    }

    private void createHabit() {
        switchMode(Mode.edit, null);
    }

    private void save(Habit habit) {
        if (habit == null) {
            habit = new Habit();
        }
        habit.setTitle(titleTextField.getText());
        habit.setDescription(descTextArea.getText());
        habit.setType(typeComboBox.getValue());
        habit.setActive(activeCheckBox.isSelected());
        habit.setHabitGroup(groupComboBox.getValue());
        Core.getInstance().habitsService().saveHabit(habit);

        if (habit.getType() == HabitType.enumeration) {
            for (EnumElement enumElement : enumElements) {
                enumElement.setHabit(habit);
                Core.getInstance().habitsService().saveEnumElement(enumElement);
            }
        }

        fillHabitTable();
        switchMode(Mode.show, habit);
    }

    private void switchMode(Mode mode, Habit habit) {
        showPane.setManaged(mode == Mode.show);
        showPane.setVisible(mode == Mode.show);
        editPane.setManaged(mode == Mode.edit);
        editPane.setVisible(mode == Mode.edit);

        if (habit != null && mode == Mode.show) {
            titleLabel.setText(habit.getExtendedTitle());
            descLabel.setText(habit.getDescription());
            typeLabel.setText("type: " + habit.getType());
        } else if (habit != null && mode == Mode.edit) {
            titleTextField.setText(habit.getTitle());
            descTextArea.setText(habit.getDescription());
            activeCheckBox.setSelected(habit.isActive());
            typeComboBox.setValue(habit.getType());
            typeComboBox.setDisable(true);
            enumTextField.setDisable(true);
            addEnumButton.setDisable(true);
            groupComboBox.setValue(habit.getHabitGroup());
            groupComboBox.setDisable(true);
            saveButton.setOnAction(event -> save(habit));
        } else if (habit == null && mode == Mode.show) {
            titleLabel.setText("");
            descLabel.setText("");
            typeLabel.setText("");
        } else if (habit == null && mode == Mode.edit) {
            titleTextField.clear();
            descTextArea.clear();
            activeCheckBox.setSelected(true);
            typeComboBox.setValue(HabitType.bollean);
            typeComboBox.setDisable(false);
            saveButton.setOnAction(event -> save(null));
        }
    }
}
