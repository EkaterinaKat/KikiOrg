package com.katyshevtseva.kikiorg.view.controller.finance;

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
        addAccountButton.setDisable(true);
        addSourceButton.setDisable(true);
        sourceTitleField.textProperty().addListener((observable, oldValue, newValue) -> setButtonsAccessibility());
        sourceDescField.textProperty().addListener((observable, oldValue, newValue) -> setButtonsAccessibility());
        accountTitleField.textProperty().addListener((observable, oldValue, newValue) -> setButtonsAccessibility());
        accountDescField.textProperty().addListener((observable, oldValue, newValue) -> setButtonsAccessibility());
    }

    private void setButtonsAccessibility() {
        addSourceButton.setDisable(sourceTitleField.getText().trim().equals("")
                || sourceDescField.getText().trim().equals(""));
        addAccountButton.setDisable(accountTitleField.getText().trim().equals("")
                || accountDescField.getText().trim().equals(""));
    }

    private void addAccount() {

    }

    private void addSource() {

    }

    void updateDisplayedData() {

    }
}
