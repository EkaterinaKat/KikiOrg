package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.finance.FinanceManager;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


class AccountsSubmodeController implements FxController {
    private ReplenishmentSubmodeController replenishmentController;
    private ExpensesSubmodeController expensesController;
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextField accountDescField;
    @FXML
    private Button addAccountButton;

    AccountsSubmodeController(ReplenishmentSubmodeController replenishmentController,
                              ExpensesSubmodeController expensesController) {
        this.replenishmentController = replenishmentController;
        this.expensesController = expensesController;
    }

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescField);
    }

    private void addAccount() {
        FinanceManager.getInstance().addAccount(accountTitleField.getText(), accountDescField.getText());
        accountTitleField.clear();
        accountDescField.clear();
        replenishmentController.setAccountComboBoxItems();
        expensesController.setAccountComboBoxItems();
    }
}
