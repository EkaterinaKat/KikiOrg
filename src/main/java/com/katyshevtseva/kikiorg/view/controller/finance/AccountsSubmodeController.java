package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


class AccountsSubmodeController implements FxController {
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextField accountDescField;
    @FXML
    private Button addAccountButton;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescField);
    }

    private void addAccount() {

    }

    void updateDisplayedData() {

    }
}
