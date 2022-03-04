package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.Sphere;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CourseEditController implements FxController {
    private final CourseOfAction courseOfAction;
    private final OneArgKnob<CourseOfAction> boardUpdateKnob;
    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<CourseOfActionStatus> statusComboBox;
    @FXML
    private ComboBox<Sphere> sphereComboBox;
    @FXML
    private TextArea descTextArea;
    @FXML
    private Button saveButton;

    CourseEditController(CourseOfAction courseOfAction, OneArgKnob<CourseOfAction> boardUpdateKnob) {
        this.courseOfAction = courseOfAction;
        this.boardUpdateKnob = boardUpdateKnob;
    }

    @FXML
    private void initialize() {
        FxUtils.associateButtonWithControls(saveButton, statusComboBox, titleTextField, sphereComboBox);
        FxUtils.setComboBoxItems(statusComboBox, CourseOfActionStatus.values());
        FxUtils.setComboBoxItems(sphereComboBox, Sphere.values());
        saveButton.setOnAction(event -> {
            CourseOfAction course;
            if (courseOfAction == null) {
                course = Core.getInstance().courseOfActionService().create(titleTextField.getText(), descTextArea.getText(),
                        statusComboBox.getValue(), sphereComboBox.getValue());
            } else {
                course = Core.getInstance().courseOfActionService().edit(courseOfAction, titleTextField.getText(),
                        descTextArea.getText(), statusComboBox.getValue(), sphereComboBox.getValue());
            }
            boardUpdateKnob.execute(course);
            FxUtils.closeWindowThatContains(saveButton);
        });
        if (courseOfAction != null) {
            titleTextField.setText(courseOfAction.getTitle());
            descTextArea.setText(courseOfAction.getDescription());
            statusComboBox.setValue(courseOfAction.getStatus());
            sphereComboBox.setValue(courseOfAction.getSphere());
        }
    }
}
