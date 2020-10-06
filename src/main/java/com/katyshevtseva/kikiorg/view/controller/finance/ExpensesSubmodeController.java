package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.finance.FinanceManager;
import com.katyshevtseva.kikiorg.core.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.finance.entity.Item;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;

class ExpensesSubmodeController implements FxController {
    @FXML
    private TextField itemTitleField;
    @FXML
    private TextArea itemDescArea;
    @FXML
    private Button addItemButton;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private ComboBox<Item> itemComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button doneButton;

    @FXML
    private void initialize() {
        Utils.disableNonNumericChars(amountTextField);
        addItemButton.setOnAction(event -> addItem());
        doneButton.setOnAction(event -> saveExpense());
        Utils.associateButtonWithControls(addItemButton, itemTitleField, itemDescArea);
        Utils.associateButtonWithControls(doneButton, amountTextField, accountComboBox, itemComboBox, datePicker);
        setItemComboBoxItems();
        setAccountComboBoxItems();
    }

    private void setItemComboBoxItems(){
//        ObservableList<Item> items = FXCollections.observableArrayList(FinanceManager.getInstance().getItems());
//        itemComboBox.setItems(items);
    }

    void setAccountComboBoxItems(){
//        ObservableList<Account> accounts = FXCollections.observableArrayList(FinanceManager.getInstance().getAccounts());
//        accountComboBox.setItems(accounts);
    }

    private void addItem() {
        FinanceManager.getInstance().addItem(itemTitleField.getText(), itemDescArea.getText());
        itemTitleField.clear();
        itemDescArea.clear();
        setItemComboBoxItems();
    }

    private void saveExpense() {
        FinanceManager.getInstance().addExpence(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                itemComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
    }
}
