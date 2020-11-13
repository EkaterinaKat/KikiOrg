package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.modes.finance.entity.CheckLine;
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
        List<CheckLine> checkLines = Core.getInstance().financeService().getCheckLine();
        trios = new ArrayList<>();
        trios.add(new Trio(amountField1, titleField1, checkLines, 0));
        trios.add(new Trio(amountField2, titleField2, checkLines, 1));
        trios.add(new Trio(amountField3, titleField3, checkLines, 2));
        trios.add(new Trio(amountField4, titleField4, checkLines, 3));
        trios.add(new Trio(amountField5, titleField5, checkLines, 4));
        trios.add(new Trio(amountField6, titleField6, checkLines, 5));
        trios.add(new Trio(amountField7, titleField7, checkLines, 6));
    }

    private void checkButtonListener() {
        rewriteCheckLines();
        showCheckResults();
    }

    private void showCheckResults() {
        if (accountComboBox.getValue() != null) {
            int amountSum = 0;
            for (Trio trio : trios) {
                amountSum += trio.getAmount();
            }
            resultLabel.setText(Core.getInstance().financeService().check(accountComboBox.getValue(), amountSum));
        }
    }

    private void rewriteCheckLines() {
        List<CheckLine> checkLinesToSave = new ArrayList<>();
        for (Trio trio : trios) {
            CheckLine checkLine = trio.getChangedCheckLine();
            if (checkLine != null) {
                checkLinesToSave.add(checkLine);
            }
        }
        Core.getInstance().financeService().rewriteCheckLine(checkLinesToSave);
    }

    void setAccountComboBoxItems() {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(Core.getInstance().financeService().getAccounts());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }

    private class Trio {
        TextField amountField;
        TextField titleField;
        CheckLine checkLine = new CheckLine();

        Trio(TextField amountField, TextField titleField, List<CheckLine> checkLines, int index) {
            this.amountField = amountField;
            this.titleField = titleField;

            if (checkLines.size() > index) {
                this.checkLine = checkLines.get(index);
                titleField.setText(checkLine.getTitle());
                amountField.setText(checkLine.getAmount().toString());
            }
        }

        CheckLine getChangedCheckLine() {
            if (!amountField.getText().trim().equals("") && !titleField.getText().trim().equals("")) {
                checkLine.setAmount(Long.parseLong(amountField.getText().trim()));
                checkLine.setTitle(titleField.getText().trim());
                return checkLine;
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
