package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.Project;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

class ProjectDialogController implements FxController {
    private NoArgsKnob tableUpdateKnob;
    private Project project;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField codeTextField;
    @FXML
    private ColorPicker colorPicker;
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
            colorPicker.setValue(Color.color(project.getColor().getRed(), project.getColor().getGreen(), project.getColor().getBlue()));
        }
        saveButton.setOnAction(event -> {
            if (project == null)
                project = new Project();
            project.setTitle(titleTextField.getText());
            project.setCode(codeTextField.getText());
            project.setDescription(descTextArea.getText());
            project.setColor(OrgUtils.getColorEntity(colorPicker.getValue()));
            Core.getInstance().trackerService().saveProject(project);

            tableUpdateKnob.execute();
            FxUtils.closeWindowThatContains(saveButton);
        });
    }
}
