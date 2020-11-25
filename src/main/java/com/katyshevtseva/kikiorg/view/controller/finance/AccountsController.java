package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


class AccountsController implements FxController {
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextArea accountDescArea;
    @FXML
    private Button addAccountButton;
    @FXML
    private Button validationButton;
    @FXML
    private TextArea reportTextArea;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescArea);
        validationButton.setOnAction(event -> Core.getInstance().financeService().validateAllAccountsAmount());
        reportTextArea.setText(Core.getInstance().financeReportService().getReport());
    }

    private void addAccount() {
        Core.getInstance().financeService().addAccount(
                accountTitleField.getText(),
                accountDescArea.getText());
        accountTitleField.clear();
        accountDescArea.setText("-");
        reportTextArea.setText(Core.getInstance().financeReportService().getReport());
    }
}
