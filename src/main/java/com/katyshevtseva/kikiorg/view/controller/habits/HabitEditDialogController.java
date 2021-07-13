package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;

class HabitEditDialogController implements FxController {
    @FXML
    private TextField titleTextField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;
    private Habit habit;
    private OneArgKnob<Habit> habitSaveHandler;

    HabitEditDialogController(Habit habitToEdit, OneArgKnob<Habit> habitSaveHandler) {
        this.habit = habitToEdit;
        this.habitSaveHandler = habitSaveHandler;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, titleTextField, descTextArea);
        tuneControls();
        saveButton.setOnAction(event -> {
            if (needToAskAboutDesc())
                new StandardDialogBuilder().openQuestionDialog("Create new habit description?", this::saveAndCloseDialog);
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
        }
    }

    private void saveAndCloseDialog(boolean createNewDeck) {
        if (habit == null) {
            habit = new Habit();
        }
        habit.setTitle(titleTextField.getText());
        habit.setActive(activeCheckBox.isSelected());
        Core.getInstance().habitsService().saveHabit(habit);
        Core.getInstance().habitsService().newHabitDesc(habit, descTextArea.getText(), createNewDeck);

        habitSaveHandler.execute(Core.getInstance().habitsService().getHabitById(habit.getId()));
        closeWindowThatContains(saveButton);
    }
}
