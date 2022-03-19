package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.TransferService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;

class TransferController implements FxController {
    private final TransferService transferService = Core.getInstance().currencyService();
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

    TransferController(NoArgsKnob operationListener) {
        this.operationListener = operationListener;
    }

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItems(fromComboBox, Core.getInstance().financeService().getActiveAccounts());
        FxUtils.setComboBoxItems(toComboBox, Core.getInstance().financeService().getActiveAccounts());
        disableNonNumericChars(amountGoneTextField);
        disableNonNumericChars(amountCameTextField);
        associateButtonWithControls(transferButton, amountGoneTextField, amountCameTextField, fromComboBox, toComboBox, datePicker);
        transferButton.setOnAction(event -> transfer());
        datePicker.setValue(new java.sql.Date(DateCorrector.getProperDate().getTime()).toLocalDate());
        adjustIntercurrencyTransferMechanism();
    }

    private void transfer() {
        transferService.addTransfer(
                fromComboBox.getValue(),
                toComboBox.getValue(),
                Long.parseLong(amountGoneTextField.getText()),
                Long.parseLong(amountCameTextField.getText()),
                java.sql.Date.valueOf(datePicker.getValue()));
        amountGoneTextField.clear();
        amountCameTextField.clear();
        operationListener.execute();
    }

    private void adjustIntercurrencyTransferMechanism() {
        changeAmountGoneTextFieldListener = (observable, oldValue, newValue) -> amountCameTextField.setText(amountGoneTextField.getText());
        amountCameTextField.setDisable(true);
        fromComboBox.setOnAction(event -> switchBetweenTypesOfTransfer());
        toComboBox.setOnAction(event -> switchBetweenTypesOfTransfer());
    }

    private void switchBetweenTypesOfTransfer() {
        if (transferService.isIntercurrencyTransfer(fromComboBox.getValue(), toComboBox.getValue())) {
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
