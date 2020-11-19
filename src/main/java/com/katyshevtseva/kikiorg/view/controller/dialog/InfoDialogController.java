package com.katyshevtseva.kikiorg.view.controller.dialog;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class InfoDialogController implements FxController {
    @FXML
    private Button okButton;
    @FXML
    private Label infoLabel;
    private final String info;

    public InfoDialogController(String info) {
        this.info = info;
    }

    @FXML
    private void initialize() {
        infoLabel.setText(info);
        okButton.setOnAction(event -> Utils.closeWindowThatContains(infoLabel));
    }
}
