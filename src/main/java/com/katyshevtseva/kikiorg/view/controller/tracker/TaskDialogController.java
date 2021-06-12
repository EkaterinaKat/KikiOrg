package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

class TaskDialogController implements FxController {
    private NoArgsKnob boardUpdateKnob;
    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<Project> projectComboBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;

    TaskDialogController(NoArgsKnob boardUpdateKnob) {
        this.boardUpdateKnob = boardUpdateKnob;
    }

    @FXML
    private void initialize() {
        FxUtils.associateButtonWithControls(saveButton, descTextArea, projectComboBox, titleTextField);
        FxUtils.setComboBoxItems(projectComboBox, Core.getInstance().trackerService().getAllProjects());
        saveButton.setOnAction(event -> {
            Core.getInstance().trackerService().createTask(titleTextField.getText(), descTextArea.getText(), projectComboBox.getValue());
            boardUpdateKnob.execute();
            FxUtils.closeWindowThatContains(saveButton);
        });
    }
}
