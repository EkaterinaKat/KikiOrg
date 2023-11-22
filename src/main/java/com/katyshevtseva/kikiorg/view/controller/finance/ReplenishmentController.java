package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static com.katyshevtseva.fx.FxUtils.*;

class ReplenishmentController implements FxController {
    private final Replenishment replenishment;
    private final NoArgsKnob operationListener;
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

    public ReplenishmentController(Replenishment replenishment, NoArgsKnob operationListener) {
        this.replenishment = replenishment;
        this.operationListener = operationListener;
    }

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveReplenishment());
        associateButtonWithControls(doneButton, amountTextField, sourceComboBox, accountComboBox, datePicker);
        adjustComboBoxes();
        datePicker.setValue(LocalDate.now());

        if (replenishment != null) {
            amountTextField.setText(replenishment.getAmount() + "");
            sourceComboBox.setValue(replenishment.getSource());
            accountComboBox.setValue(replenishment.getAccount());
            FxUtils.setDate(datePicker, replenishment.getDate());
        }
    }

    public void adjustComboBoxes() {
        setComboBoxItems(sourceComboBox, Core.getInstance().financeService().getAllSources());
        setComboBoxItemsAndSetSelectedFirstItem(accountComboBox, Core.getInstance().financeService().getActiveAccounts());
    }

    private void saveReplenishment() {
        if (replenishment == null) {
            Core.getInstance().financeService().addReplenishment(
                    accountComboBox.getValue(),
                    Long.parseLong(amountTextField.getText()),
                    sourceComboBox.getValue(),
                    java.sql.Date.valueOf(datePicker.getValue()));
            amountTextField.clear();
        } else {
            Core.getInstance().financeService().editReplenishment(
                    replenishment,
                    accountComboBox.getValue(),
                    Long.parseLong(amountTextField.getText()),
                    sourceComboBox.getValue(),
                    java.sql.Date.valueOf(datePicker.getValue()));
            FxUtils.closeWindowThatContains(accountComboBox);
        }
        operationListener.execute();
    }
}
