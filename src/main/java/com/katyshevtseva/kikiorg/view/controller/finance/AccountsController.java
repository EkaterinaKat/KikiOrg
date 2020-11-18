package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.Owner;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private ComboBox<Owner> ownerComboBox;
    @FXML
    private TextArea reportTextArea;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescArea, ownerComboBox);
        validationButton.setOnAction(event -> Core.getInstance().financeService().validateAllAccountsAmount());
        ownerComboBox.setItems(FXCollections.observableArrayList(Core.getInstance().financeService().getAvailableAccountOwners()));
        reportTextArea.setText(Core.getInstance().financeReportService().getReport());
    }

    private void addAccount() {
        Core.getInstance().financeService().addAccount(
                accountTitleField.getText(),
                accountDescArea.getText(),
                ownerComboBox.getValue());
        accountTitleField.clear();
        accountDescArea.clear();
        reportTextArea.setText(Core.getInstance().financeReportService().getReport());
    }
}
