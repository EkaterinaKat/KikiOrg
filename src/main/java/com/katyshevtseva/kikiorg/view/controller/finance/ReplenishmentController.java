package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static com.katyshevtseva.fx.FxUtils.*;

class ReplenishmentController implements FxController {
    private NoArgsKnob operationListener;
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

    ReplenishmentController(NoArgsKnob operationListener) {
        this.operationListener = operationListener;
    }

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveReplenishment());
        associateButtonWithControls(doneButton, amountTextField, sourceComboBox, accountComboBox, datePicker);
        setComboBoxItems(sourceComboBox, Core.getInstance().financeService().getAllSources());
        setComboBoxItemsAndSetSelectedFirstItem(accountComboBox, Core.getInstance().financeService().getActiveAccounts());
        datePicker.setValue(LocalDate.now());
    }

    private void saveReplenishment() {
        Core.getInstance().financeService().addReplenishment(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                sourceComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
        operationListener.execute();
    }
}
