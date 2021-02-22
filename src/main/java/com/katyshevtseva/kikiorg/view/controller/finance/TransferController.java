package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDate;

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
    private DatePicker datePicker;

    @FXML
    private void initialize() {
        adjustComboBox(fromComboBox);
        adjustComboBox(toComboBox);
        setAccountComboBoxItems(fromComboBox);
        setAccountComboBoxItems(toComboBox);
        Utils.disableNonNumericChars(amountTextField);
        Utils.associateButtonWithControls(transferButton, amountTextField, fromComboBox, toComboBox, datePicker);
        transferButton.setOnAction(event -> transfer());
        datePicker.setValue(LocalDate.now());
    }

    private void transfer() {
        Core.getInstance().financeService().addTransfer(fromComboBox.getValue(), toComboBox.getValue(),
                Long.parseLong(amountTextField.getText()), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
    }

    private void setAccountComboBoxItems(ComboBox<Account> accountComboBox) {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(
                    Core.getInstance().financeService().getAllAccounts());
            accountComboBox.setItems(accounts);
        }
    }

    // Этот метод только устанавливает отображение имени счета вместе с информацией о владельце
    private void adjustComboBox(ComboBox<Account> comboBox) {
        comboBox.setCellFactory(
                new Callback<ListView<Account>, ListCell<Account>>() {
                    @Override
                    public ListCell<Account> call(ListView<Account> p) {
                        return new ListCell<Account>() {
                            @Override
                            protected void updateItem(Account item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setText("");
                                } else {
                                    setText(item.getTitleWithOwnerInfo());
                                }
                            }
                        };
                    }
                });

        comboBox.setButtonCell(
                new ListCell<Account>() {
                    @Override
                    protected void updateItem(Account t, boolean bln) {
                        super.updateItem(t, bln);
                        if (bln) {
                            setText("");
                        } else {
                            setText(t.getTitleWithOwnerInfo());
                        }
                    }
                });
    }
}
