package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

class ProjectDialogController implements FxController {
    private NoArgsKnob tableUpdateKnob;
    private Project project;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField codeTextField;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;

    ProjectDialogController(NoArgsKnob tableUpdateKnob, Project project) {
        this.tableUpdateKnob = tableUpdateKnob;
        this.project = project;
    }

    @FXML
    private void initialize() {
        FxUtils.associateButtonWithControls(saveButton, titleTextField, descTextArea, codeTextField);
        if (project != null) {
            titleTextField.setText(project.getTitle());
            codeTextField.setText(project.getCode());
            descTextArea.setText(project.getDescription());
        }
        saveButton.setOnAction(event -> {
            if (project == null)
                project = new Project();
            project.setTitle(titleTextField.getText());
            project.setCode(codeTextField.getText());
            project.setDescription(descTextArea.getText());
            Core.getInstance().trackerService().saveProject(project);

            tableUpdateKnob.execute();
            FxUtils.closeWindowThatContains(saveButton);
        });
    }
}
