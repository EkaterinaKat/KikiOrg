package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

class ExpensesSubmodeController implements FxController {
    @FXML
    private TextField itemTitleField;
    @FXML
    private TextArea itemDescArea;
    @FXML
    private Button addItemButton;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private ComboBox<String> itemComboBox;
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
    }

    void updateDisplayedData() {

    }

    private void addItem() {

    }

    private void saveExpense() {

    }
}
