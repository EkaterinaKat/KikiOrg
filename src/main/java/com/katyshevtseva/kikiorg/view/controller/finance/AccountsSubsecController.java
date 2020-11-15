package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.Owner;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


class AccountsSubsecController implements FxController {
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextArea accountDescArea;
    @FXML
    private Button addAccountButton;
    @FXML
    private ComboBox<Account> fromComboBox;
    @FXML
    private ComboBox<Account> toComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private Button transferButton;
    @FXML
    private Button validationButton;
    @FXML
    private ComboBox<Owner> ownerComboBox;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescArea, ownerComboBox);
        setAccountComboBoxItems(fromComboBox);
        setAccountComboBoxItems(toComboBox);
        Utils.disableNonNumericChars(amountTextField);
        Utils.associateButtonWithControls(transferButton, amountTextField, fromComboBox, toComboBox);
        transferButton.setOnAction(event -> transfer());
        validationButton.setOnAction(event -> Core.getInstance().financeService().validateAllAccountsAmount());
        ownerComboBox.setItems(FXCollections.observableArrayList(Core.getInstance().financeService().getAvailableAccountOwners()));
    }

    private void transfer() {
        Core.getInstance().financeService().makeTransfer(fromComboBox.getValue(), toComboBox.getValue(),
                Long.parseLong(amountTextField.getText()));
        amountTextField.clear();
    }

    private void addAccount() {
        Core.getInstance().financeService().addAccount(
                accountTitleField.getText(),
                accountDescArea.getText(),
                ownerComboBox.getValue());
        accountTitleField.clear();
        accountDescArea.clear();
        setAccountComboBoxItems(fromComboBox);
        setAccountComboBoxItems(toComboBox);
    }

    private void setAccountComboBoxItems(ComboBox<Account> accountComboBox) {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(Core.getInstance().financeService().getAccounts());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }
}
