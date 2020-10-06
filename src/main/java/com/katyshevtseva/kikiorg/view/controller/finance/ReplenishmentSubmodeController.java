package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.finance.FinanceManager;
import com.katyshevtseva.kikiorg.core.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.finance.entity.Source;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

class ReplenishmentSubmodeController implements FxController {
    @FXML
    private TextField sourceTitleField;
    @FXML
    private TextArea sourceDescArea;
    @FXML
    private Button addSourceButton;
    @FXML
    private TextField amountTextField;
    @FXML
    private ComboBox<Source> sourceComboBox;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button doneButton;

    @FXML
    private void initialize() {
        Utils.disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveReplenishment());
        addSourceButton.setOnAction(event -> addSource());
        Utils.associateButtonWithControls(doneButton, amountTextField, sourceComboBox, accountComboBox, datePicker);
        Utils.associateButtonWithControls(addSourceButton, sourceTitleField, sourceDescArea);
        setSourceComboBoxItems();
        setAccountComboBoxItems();
    }

    private void setSourceComboBoxItems(){
        ObservableList<Source> sources = FXCollections.observableArrayList(FinanceManager.getInstance().getSources());
        sourceComboBox.setItems(sources);
    }

    void setAccountComboBoxItems(){
        ObservableList<Account> accounts = FXCollections.observableArrayList(FinanceManager.getInstance().getAccounts());
        accountComboBox.setItems(accounts);
    }

    private void addSource() {
        FinanceManager.getInstance().addSourse(sourceTitleField.getText(), sourceDescArea.getText());
        sourceTitleField.clear();
        sourceDescArea.clear();
        setSourceComboBoxItems();
    }

    private void saveReplenishment() {
        FinanceManager.getInstance().addReplenishment(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                sourceComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
    }
}
