package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.modes.finance.FinanceManager;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.AccountPart;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CheckSubmodeController implements FxController {
    private List<TextField> textFields = new ArrayList<>();
    @FXML
    private TextField amountField1;
    @FXML
    private TextField amountField2;
    @FXML
    private TextField amountField3;
    @FXML
    private TextField amountField4;
    @FXML
    private TextField amountField5;
    @FXML
    private TextField amountField6;
    @FXML
    private TextField amountField7;
    @FXML
    private TextField titleField1;
    @FXML
    private TextField titleField2;
    @FXML
    private TextField titleField3;
    @FXML
    private TextField titleField4;
    @FXML
    private TextField titleField5;
    @FXML
    private TextField titleField6;
    @FXML
    private TextField titleField7;
    @FXML
    private Button checkButton;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private Label resultLabel;
    private List<Trio> trios;

    @FXML
    private void initialize() {
        textFields.addAll(Arrays.asList(amountField1, amountField2, amountField3, amountField4,
                amountField5, amountField6, amountField7));
        textFields.forEach(Utils::disableNonNumericChars);
        checkButton.setOnAction(event -> checkButtonListener());
        setAccountComboBoxItems();
        createTrios();
    }

    private void createTrios() {
        List<AccountPart> accountParts = FinanceManager.getInstance().getAccountParts();
        trios = new ArrayList<>();
        trios.add(new Trio(amountField1, titleField1, accountParts, 0));
        trios.add(new Trio(amountField2, titleField2, accountParts, 1));
        trios.add(new Trio(amountField3, titleField3, accountParts, 2));
        trios.add(new Trio(amountField4, titleField4, accountParts, 3));
        trios.add(new Trio(amountField5, titleField5, accountParts, 4));
        trios.add(new Trio(amountField6, titleField6, accountParts, 5));
        trios.add(new Trio(amountField7, titleField7, accountParts, 6));
    }

    private void checkButtonListener() {
        rewriteAccountParts();
        showCheckResults();
    }

    private void showCheckResults() {
        if (accountComboBox.getValue() != null) {
            int amountSum = 0;
            for (Trio trio : trios) {
                amountSum += trio.getAmount();
            }
            resultLabel.setText(FinanceManager.getInstance().check(accountComboBox.getValue(), amountSum));
        }
    }

    private void rewriteAccountParts() {
        List<AccountPart> accountPartsToSave = new ArrayList<>();
        for (Trio trio : trios) {
            AccountPart accountPart = trio.getChangedAccountPart();
            if (accountPart != null) {
                accountPartsToSave.add(accountPart);
            }
        }
        FinanceManager.getInstance().rewriteAccountParts(accountPartsToSave);
    }

    void setAccountComboBoxItems() {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(FinanceManager.getInstance().getAccounts());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }

    private class Trio {
        TextField amountField;
        TextField titleField;
        AccountPart accountPart = new AccountPart();

        Trio(TextField amountField, TextField titleField, List<AccountPart> accountParts, int index) {
            this.amountField = amountField;
            this.titleField = titleField;

            if (accountParts.size() > index) {
                this.accountPart = accountParts.get(index);
                titleField.setText(accountPart.getTitle());
                amountField.setText(accountPart.getAmount().toString());
            }
        }

        AccountPart getChangedAccountPart() {
            if (!amountField.getText().trim().equals("") && !titleField.getText().trim().equals("")) {
                accountPart.setAmount(Long.parseLong(amountField.getText().trim()));
                accountPart.setTitle(titleField.getText().trim());
                return accountPart;
            }
            return null;
        }

        int getAmount() {
            if (!amountField.getText().trim().equals("")) {
                return Integer.parseInt(amountField.getText().trim());
            }
            return 0;
        }
    }
}
