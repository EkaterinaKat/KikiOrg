package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


class AccountsSubmodeController implements Controller {
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextField accountDescField;
    @FXML
    private Button addAccountButton;
    @FXML
    private TextField sourceTitleField;
    @FXML
    private TextField sourceDescField;
    @FXML
    private Button addSourceButton;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        addSourceButton.setOnAction(event -> addSource());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescField);
        Utils.associateButtonWithControls(addSourceButton, sourceTitleField, sourceDescField);
    }

    private void addAccount() {

    }

    private void addSource() {

    }

    void updateDisplayedData() {

    }
}
