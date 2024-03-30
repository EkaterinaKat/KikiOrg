package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;

class TransferController implements FxController {
    private final FinanceService service = Core.getInstance().financeService;
    private final Transfer transfer;
    private final NoArgsKnob operationListener;
    private ChangeListener<String> changeAmountGoneTextFieldListener;
    @FXML
    private ComboBox<Account> fromComboBox;
    @FXML
    private ComboBox<Account> toComboBox;
    @FXML
    private TextField amountGoneTextField;
    @FXML
    private TextField amountCameTextField;
    @FXML
    private Button transferButton;
    @FXML
    private DatePicker datePicker;

    public TransferController(Transfer transfer, NoArgsKnob operationListener) {
        this.transfer = transfer;
        this.operationListener = operationListener;
    }

    @FXML
    private void initialize() {
        adjustComboBoxes();
        disableNonNumericChars(amountGoneTextField);
        disableNonNumericChars(amountCameTextField);
        associateButtonWithControls(transferButton, amountGoneTextField, amountCameTextField, fromComboBox, toComboBox, datePicker);
        transferButton.setOnAction(event -> transfer());
        datePicker.setValue(LocalDate.now());
        adjustIntercurrencyTransferMechanism();

        if (transfer != null) {
            fromComboBox.setValue(transfer.getFrom());
            toComboBox.setValue(transfer.getTo());
            amountGoneTextField.setText(transfer.getGoneAmount() + "");
            amountCameTextField.setText(transfer.getCameAmount() + "");
            FxUtils.setDate(datePicker, transfer.getDate());
        }
    }

    public void adjustComboBoxes() {
        FxUtils.setComboBoxItems(fromComboBox, service.getActiveAccounts());
        FxUtils.setComboBoxItems(toComboBox, service.getActiveAccounts());
    }

    private void transfer() {
        if (transfer == null) {
            service.addTransfer(
                    fromComboBox.getValue(),
                    toComboBox.getValue(),
                    Long.parseLong(amountGoneTextField.getText()),
                    Long.parseLong(amountCameTextField.getText()),
                    java.sql.Date.valueOf(datePicker.getValue()));
            amountGoneTextField.clear();
            amountCameTextField.clear();
        } else {
            service.editTransfer(
                    transfer,
                    fromComboBox.getValue(),
                    toComboBox.getValue(),
                    Long.parseLong(amountGoneTextField.getText()),
                    Long.parseLong(amountCameTextField.getText()),
                    java.sql.Date.valueOf(datePicker.getValue()));
            FxUtils.closeWindowThatContains(amountCameTextField);
        }
        operationListener.execute();
    }

    private void adjustIntercurrencyTransferMechanism() {
        changeAmountGoneTextFieldListener = (observable, oldValue, newValue) -> amountCameTextField.setText(amountGoneTextField.getText());
        amountCameTextField.setDisable(true);
        fromComboBox.setOnAction(event -> switchBetweenTypesOfTransfer());
        toComboBox.setOnAction(event -> switchBetweenTypesOfTransfer());
    }

    private void switchBetweenTypesOfTransfer() {
        if (service.isIntercurrencyTransfer(fromComboBox.getValue(), toComboBox.getValue())) {
            amountCameTextField.setDisable(false);
            amountCameTextField.clear();
            amountGoneTextField.textProperty().removeListener(changeAmountGoneTextFieldListener);
        } else {
            amountCameTextField.setDisable(true);
            amountCameTextField.setText(amountGoneTextField.getText());
            amountGoneTextField.textProperty().addListener(changeAmountGoneTextFieldListener);
        }
    }
}
