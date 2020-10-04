package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;

class ReplenishmentSubmodeController implements WindowBuilder.Controller {
    @FXML
    private TextField sourceTitleField;
    @FXML
    private TextArea sourceDescArea;
    @FXML
    private Button addSourceButton;
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
        addSourceButton.setOnAction(event -> addSource());
        Utils.associateButtonWithControls(doneButton, amountTextField, sourceComboBox, accountComboBox, datePicker);
        Utils.associateButtonWithControls(addSourceButton, sourceTitleField, sourceDescArea);
    }

    void updateDisplayedData() {

    }

    private void saveReplenishment() {

    }

    private void addSource() {

    }
}
