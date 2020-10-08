package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.finance.FinanceManager;
import com.katyshevtseva.kikiorg.core.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Только первичная настройка выполнена */
class CheckSubmodeController implements FxController {
    private List<TextField> textFields = new ArrayList<>();
    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private TextField textField4;
    @FXML
    private TextField textField5;
    @FXML
    private Button checkButton;
    @FXML
    private ComboBox<Account> accountComboBox;

    @FXML
    private void initialize() {
        textFields.addAll(Arrays.asList(textField1, textField2, textField3, textField4, textField5));
        textFields.forEach(Utils::disableNonNumericChars);
        checkButton.setOnAction(event -> checkButtonListener());
    }

    private void checkButtonListener() {

    }

    void setAccountComboBoxItems() {
        if(accountComboBox != null){
            ObservableList<Account> accounts = FXCollections.observableArrayList(FinanceManager.getInstance().getAccounts());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }
}
