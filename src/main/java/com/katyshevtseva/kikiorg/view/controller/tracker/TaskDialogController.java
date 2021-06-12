package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

class TaskDialogController implements FxController {
    private Task task;
    private NoArgsKnob boardUpdateKnob;
    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;

    TaskDialogController(Task task, NoArgsKnob boardUpdateKnob) {
        this.task = task;
        this.boardUpdateKnob = boardUpdateKnob;
    }

    @FXML
    private void initialize() {
        FxUtils.associateButtonWithControls(saveButton, descTextArea, projectComboBox, titleTextField);
        FxUtils.setComboBoxItems(projectComboBox, Core.getInstance().trackerService().getAllProjects());
        saveButton.setOnAction(event -> {
            if (task == null) {
                Core.getInstance().trackerService().createTask(titleTextField.getText(), descTextArea.getText(), projectComboBox.getValue());
            } else {
                task.setTitle(titleTextField.getText());
                task.setDescription(descTextArea.getText());
                Core.getInstance().trackerService().saveEditedTask(task);
            }
            boardUpdateKnob.execute();
            FxUtils.closeWindowThatContains(saveButton);
        });
        if (task != null) {
            titleTextField.setText(task.getTitle());
            descTextArea.setText(task.getDescription());
            projectComboBox.setValue(task.getProject());
            projectComboBox.setDisable(true);
        }
    }
}
