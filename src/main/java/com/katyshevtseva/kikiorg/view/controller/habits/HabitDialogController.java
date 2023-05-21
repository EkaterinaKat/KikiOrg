package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.FxUtils;
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

class HabitDialogController implements FxController {
    private final String DESC_CHANGE_ACTION_QUESTION = "Is it necessary to create description change action?";
    @FXML
    private TextField titleTextField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private TextField doneDaysTextField;
    @FXML
    private TextField totalDaysTextField;
    @FXML
    private Button saveButton;
    private Habit habit;
    private final OneArgKnob<Habit> habitSaveHandler;

    HabitDialogController(Habit habitToEdit, OneArgKnob<Habit> habitSaveHandler) {
        this.habit = habitToEdit;
        this.habitSaveHandler = habitSaveHandler;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, titleTextField, descTextArea, doneDaysTextField, totalDaysTextField);
        FxUtils.disableNonNumericChars(doneDaysTextField);
        FxUtils.disableNonNumericChars(totalDaysTextField);
        setExistingPieceInfo();
        saveButton.setOnAction(event -> {
            if (needToAskAboutDesc())
                new StandardDialogBuilder()
                        .setSize(200, 500)
                        .openQuestionDialog(DESC_CHANGE_ACTION_QUESTION, this::saveAndCloseDialog);
            else
                saveAndCloseDialog(false);
        });
    }

    private boolean needToAskAboutDesc() {
        boolean itIsHabitCreation = habit == null;
        if (itIsHabitCreation)
            return false;
        return !habit.getDescription().equals(descTextArea.getText());
    }

    private void setExistingPieceInfo() {
        if (habit != null) {
            titleTextField.setText(habit.getTitle());
            descTextArea.setText(habit.getDescription());
            activeCheckBox.setSelected(habit.isActive());
        }
    }

    private void saveAndCloseDialog(boolean needToCreateDescChangeAction) {
        habit = Core.getInstance().habitsService().saveHabit(
                habit,
                titleTextField.getText(),
                descTextArea.getText(),
                Integer.parseInt(doneDaysTextField.getText()),
                Integer.parseInt(totalDaysTextField.getText()),
                activeCheckBox.isSelected(),
                needToCreateDescChangeAction);

        habitSaveHandler.execute(Core.getInstance().habitsService().getHabitById(habit.getId()));
        closeWindowThatContains(saveButton);
    }
}
