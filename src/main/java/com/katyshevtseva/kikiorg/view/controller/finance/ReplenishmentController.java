package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

class ReplenishmentController implements FxController {
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
        datePicker.setValue(LocalDate.now());
    }

    private void setSourceComboBoxItems() {
        ObservableList<Source> sources = FXCollections.observableArrayList(Core.getInstance().financeService().getSources());
        sourceComboBox.setItems(sources);
    }

    private void setAccountComboBoxItems() {
        if(accountComboBox != null){
            ObservableList<Account> accounts = FXCollections.observableArrayList(Core.getInstance().financeService().getAccountsAvailableForCurrentOwner());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }

    private void addSource() {
        Core.getInstance().financeService().addSourse(sourceTitleField.getText(), sourceDescArea.getText());
        sourceTitleField.clear();
        sourceDescArea.setText("-");
        setSourceComboBoxItems();
    }

    private void saveReplenishment() {
        Core.getInstance().financeService().addReplenishment(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                sourceComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
    }
}
