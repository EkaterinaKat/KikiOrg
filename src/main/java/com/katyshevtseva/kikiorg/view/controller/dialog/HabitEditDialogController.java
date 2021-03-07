package com.katyshevtseva.kikiorg.view.controller.dialog;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HabitEditDialogController implements FxController {
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
    private Habit habit;
    private HabitSaveHandler habitSaveHandler;

    public HabitEditDialogController(Habit habitToEdit, HabitSaveHandler habitSaveHandler) {
        this.habit = habitToEdit;
        this.habitSaveHandler = habitSaveHandler;
    }

    @FXML
    private void initialize() {
        Utils.associateButtonWithControls(saveButton, titleTextField, typeComboBox, descTextArea, groupComboBox);
        Utils.associateButtonWithControls(addEnumButton, enumTextField);
        addEnumButton.setOnAction(event -> addEnumElement());
        saveButton.setOnAction(event -> save());
        setComboBoxesItems();
        tuneControls();
    }

    private void tuneControls() {
        if (habit == null) {
            addEnumButton.setDisable(true);
            enumTextField.setDisable(true);
            typeComboBox.setOnAction(event -> {
                boolean isEnum = typeComboBox.getValue() == HabitType.enumeration;
                enumTextField.setDisable(!isEnum);
                if (!isEnum) {
                    enumElements = new ArrayList<>();
                    enumLabel.setText("");
                    enumTextField.clear();
                }
            });
        } else {
            titleTextField.setText(habit.getTitle());
            descTextArea.setText(habit.getDescription());
            activeCheckBox.setSelected(habit.isActive());
            typeComboBox.setValue(habit.getType());
            typeComboBox.setDisable(true);
            groupComboBox.setValue(habit.getHabitGroup());
            groupComboBox.setDisable(true);
            enumLabel.setText(Habit.getEnumString(habit.enumElements));
            enumTextField.setDisable(habit.getType() != HabitType.enumeration);
            enumElements.addAll(habit.getEnumElements());
        }
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

    private void save() {
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

        habitSaveHandler.execute(Core.getInstance().habitsService().getHabitById(habit.getId()));
        Utils.closeWindowThatContains(typeComboBox);
    }

    @FunctionalInterface
    public interface HabitSaveHandler {
        void execute(Habit savedHabit);
    }
}
