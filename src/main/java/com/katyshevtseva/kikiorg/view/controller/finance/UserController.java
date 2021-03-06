package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.OwnerService.Owner;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.Arrays;

class UserController implements FxController {
    @FXML
    private ComboBox<Owner> userComboBox;

    @FXML
    private void initialize() {
        userComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(Owner.K, Owner.M, Owner.C)));
        userComboBox.setValue(Core.getInstance().ownerService().getCurrentOwner());
        userComboBox.valueProperty().addListener((observable ->
                Core.getInstance().ownerService().setCurrentOwner(userComboBox.getValue())));
    }
}
