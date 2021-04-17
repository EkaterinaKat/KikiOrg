package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;

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
        FxUtils.setComboBoxItems(fromComboBox, Core.getInstance().financeService().getAllAccounts());
        FxUtils.setComboBoxItems(toComboBox, Core.getInstance().financeService().getAllAccounts());
        disableNonNumericChars(amountTextField);
        associateButtonWithControls(transferButton, amountTextField, fromComboBox, toComboBox, datePicker);
        transferButton.setOnAction(event -> transfer());
        datePicker.setValue(new java.sql.Date(DateCorrector.getProperDate().getTime()).toLocalDate());
    }

    private void transfer() {
        Core.getInstance().financeService().addTransfer(fromComboBox.getValue(), toComboBox.getValue(),
                Long.parseLong(amountTextField.getText()), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
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
