package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

class TransferController implements FxController {
    @FXML
    private ComboBox<Account> fromComboBox;
    @FXML
    private ComboBox<Account> toComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private Button transferButton;

    @FXML
    private void initialize() {
        setAccountComboBoxItems(fromComboBox);
        setAccountComboBoxItems(toComboBox);
        Utils.disableNonNumericChars(amountTextField);
        Utils.associateButtonWithControls(transferButton, amountTextField, fromComboBox, toComboBox);
        transferButton.setOnAction(event -> transfer());
    }

    private void transfer() {
        Core.getInstance().financeService().addTransfer(fromComboBox.getValue(), toComboBox.getValue(),
                Long.parseLong(amountTextField.getText()));
        amountTextField.clear();
    }

    private void setAccountComboBoxItems(ComboBox<Account> accountComboBox) {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(Core.getInstance().financeService().getAccountsForCurrentUser());
            accountComboBox.setItems(accounts);
        }
    }
}
