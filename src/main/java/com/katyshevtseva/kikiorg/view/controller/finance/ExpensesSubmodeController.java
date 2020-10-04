package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

class ExpensesSubmodeController implements WindowBuilder.Controller {
    @FXML
    private TextField itemTextField;
    @FXML
    private Button addItemButton;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private ComboBox<String> itemComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private Button doneButton;

    @FXML
    private void initialize() {
        Utils.disableNonNumericChars(amountTextField);
        addItemButton.setOnAction(event -> addItem());
        doneButton.setOnAction(event -> saveExpense());
        Utils.associateButtonWithControls(addItemButton, itemTextField);
        Utils.associateButtonWithControls(doneButton, amountTextField, accountComboBox, itemComboBox);
    }

    void updateDisplayedData() {

    }

    private void addItem() {

    }

    private void saveExpense() {

    }
}
