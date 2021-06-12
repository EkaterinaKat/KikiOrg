package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.katyshevtseva.fx.FxUtils.*;

class HabitEditDialogController implements FxController {
    @FXML
    private TextField titleTextField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<HabitGroup> groupComboBox;
    private Habit habit;
    private OneArgKnob<Habit> habitSaveHandler;

    HabitEditDialogController(Habit habitToEdit, OneArgKnob<Habit> habitSaveHandler) {
        this.habit = habitToEdit;
        this.habitSaveHandler = habitSaveHandler;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, titleTextField, descTextArea, groupComboBox);
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
        if (itIsHabitCreation)
            return false;
        boolean descWasEdited = !habit.getCurrentDescription().getText().equals(descTextArea.getText());
        return descWasEdited;
    }

    private void tuneControls() {
        if (habit != null) {
            titleTextField.setText(habit.getTitle());
            descTextArea.setText(habit.getCurrentDescription() == null ? "описания нет патчимута" : habit.getCurrentDescription().getText());
            activeCheckBox.setSelected(habit.isActive());
            groupComboBox.setValue(habit.getHabitGroup());
            groupComboBox.setDisable(true);
        }
    }

    private void saveAndCloseDialog(boolean createNewDeck) {
        if (habit == null) {
            habit = new Habit();
        }
        habit.setTitle(titleTextField.getText());
        habit.setActive(activeCheckBox.isSelected());
        habit.setHabitGroup(groupComboBox.getValue());
        Core.getInstance().habitsService().saveHabit(habit);
        Core.getInstance().habitsService().newHabitDesc(habit, descTextArea.getText(), createNewDeck);

        habitSaveHandler.execute(Core.getInstance().habitsService().getHabitById(habit.getId()));
        closeWindowThatContains(saveButton);
    }
}
