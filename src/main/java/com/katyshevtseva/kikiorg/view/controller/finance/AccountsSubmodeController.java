package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.modes.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


class AccountsSubmodeController implements FxController {
    private ReplenishmentSubmodeController replenishmentController;
    private ExpensesSubmodeController expensesController;
    private CheckSubmodeController checkController;
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

    AccountsSubmodeController(ReplenishmentSubmodeController replenishmentController,
                              ExpensesSubmodeController expensesController, CheckSubmodeController checkController) {
        this.replenishmentController = replenishmentController;
        this.expensesController = expensesController;
        this.checkController = checkController;
    }

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescArea);
        setAccountComboBoxItems(fromComboBox);
        setAccountComboBoxItems(toComboBox);
        Utils.disableNonNumericChars(amountTextField);
        Utils.associateButtonWithControls(transferButton, amountTextField, fromComboBox, toComboBox);
        transferButton.setOnAction(event -> transfer());
        validationButton.setOnAction(event -> Core.getInstance().financeService().validateAllAccountsAmount());
    }

    private void transfer() {
        Core.getInstance().financeService().makeTransfer(fromComboBox.getValue(), toComboBox.getValue(),
                Long.parseLong(amountTextField.getText()));
        amountTextField.clear();
    }

    private void addAccount() {
        Core.getInstance().financeService().addAccount(accountTitleField.getText(), accountDescArea.getText(), Owner.K);
        accountTitleField.clear();
        accountDescArea.clear();
        replenishmentController.setAccountComboBoxItems();
        expensesController.setAccountComboBoxItems();
        checkController.setAccountComboBoxItems();
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
