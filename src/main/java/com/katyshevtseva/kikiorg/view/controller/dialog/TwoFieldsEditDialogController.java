package com.katyshevtseva.kikiorg.view.controller.dialog;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class TwoFieldsEditDialogController implements FxController {
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    @FXML
    private Button saveButton;
    private EventHandler eventHandler;
    private String initFirstText;
    private String initSecondText;

    public TwoFieldsEditDialogController(String initFirstText, String initSecondText, EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.initFirstText = initFirstText;
        this.initSecondText = initSecondText;
    }

    @FXML
    private void initialize() {
        Utils.associateButtonWithControls(saveButton, textArea, textField);
        textField.setText(initFirstText);
        textArea.setText(initSecondText);
        saveButton.setOnAction(event -> {
            eventHandler.execute(textField.getText(), textArea.getText());
            Utils.closeWindowThatContains(textField);
        });
    }

    @FunctionalInterface
    interface EventHandler {
        void execute(String firstText, String secondText);
    }
}
