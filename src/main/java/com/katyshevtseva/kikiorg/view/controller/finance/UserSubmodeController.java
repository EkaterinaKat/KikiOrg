package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.modes.finance.Owner;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class UserSubmodeController implements FxController {
    @FXML
    private ComboBox<Owner> userComboBox;

    @FXML
    private void initialize() {
        userComboBox.setItems(FXCollections.observableArrayList(Owner.K, Owner.M));
        userComboBox.setValue(Core.getInstance().financeService().getCurrentOwner());
        userComboBox.valueProperty().addListener((observable ->
                Core.getInstance().financeService().setCurrentOwner(userComboBox.getValue())));
    }
}