package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.EnumElement;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitType;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.*;

class HabitEditDialogController implements FxController {
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

    HabitEditDialogController(Habit habitToEdit, HabitSaveHandler habitSaveHandler) {
        this.habit = habitToEdit;
        this.habitSaveHandler = habitSaveHandler;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, titleTextField, typeComboBox, descTextArea, groupComboBox);
        associateButtonWithControls(addEnumButton, enumTextField);
        addEnumButton.setOnAction(event -> addEnumElement());
        setComboBoxItems(typeComboBox, HabitType.values());
        setComboBoxItems(groupComboBox, HabitGroup.values());
        tuneControls();
        saveButton.setOnAction(event -> {
            if (needToAskAboutDesc())
                OrgUtils.getDialogBuilder().openQuestionDialog("Create new habit description?", this::saveAndCloseDialog);
            else
                saveAndCloseDialog(false);
        });
    }

    private boolean needToAskAboutDesc() {
        boolean itIsHabitCreation = habit == null;
        if(itIsHabitCreation)
            return false;
        boolean descWasEdited = !habit.getCurrentDescription().getText().equals(descTextArea.getText());
        return descWasEdited;
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
            descTextArea.setText(habit.getCurrentDescription() == null ? "описания нет патчимута" : habit.getCurrentDescription().getText());
            activeCheckBox.setSelected(habit.isActive());
            typeComboBox.setValue(habit.getType());
            typeComboBox.setDisable(true);
            groupComboBox.setValue(habit.getHabitGroup());
            groupComboBox.setDisable(true);
            enumLabel.setText(Habit.getEnumString(habit.getEnumElements()));
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

    private void saveAndCloseDialog(boolean createNewDeck) {
        if (habit == null) {
            habit = new Habit();
        }
        habit.setTitle(titleTextField.getText());
        habit.setType(typeComboBox.getValue());
        habit.setActive(activeCheckBox.isSelected());
        habit.setHabitGroup(groupComboBox.getValue());
        Core.getInstance().habitsService().saveHabit(habit);
        Core.getInstance().habitsService().newHabitDesc(habit, descTextArea.getText(), createNewDeck);

        if (habit.getType() == HabitType.enumeration) {
            for (EnumElement enumElement : enumElements) {
                enumElement.setHabit(habit);
                Core.getInstance().habitsService().saveEnumElement(enumElement);
            }
        }

        habitSaveHandler.execute(Core.getInstance().habitsService().getHabitById(habit.getId()));
        closeWindowThatContains(typeComboBox);
    }

    @FunctionalInterface
    public interface HabitSaveHandler {
        void execute(Habit savedHabit);
    }
}
