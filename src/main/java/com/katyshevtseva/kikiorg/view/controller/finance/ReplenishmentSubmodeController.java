package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

class ReplenishmentSubmodeController implements WindowBuilder.Controller {
    @FXML
    private TextField amountTextField;
    @FXML
    private ComboBox<String> sourceComboBox;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button doneButton;

    @FXML
    private void initialize() {
        Utils.disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveReplenishment());
        doneButton.setDisable(true);
        amountTextField.textProperty().addListener(observable -> setDoneButtonAccessibility());
        sourceComboBox.valueProperty().addListener((observable -> setDoneButtonAccessibility()));
        accountComboBox.valueProperty().addListener(observable -> setDoneButtonAccessibility());
        datePicker.valueProperty().addListener(observable -> setDoneButtonAccessibility());
    }

    private void setDoneButtonAccessibility() {
        doneButton.setDisable(amountTextField.getText().trim().equals("") || sourceComboBox.getValue() == null
                || accountComboBox.getValue() == null || datePicker.getValue() == null);
    }


    void updateDisplayedData() {

    }

    private void saveReplenishment() {

    }
}
