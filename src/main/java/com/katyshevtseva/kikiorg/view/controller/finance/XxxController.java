package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class XxxController implements FxController {
    @FXML
    private ComboBox<Account> accountToDeleteComboBox;
    @FXML
    private ComboBox<Account> substituteAccountComboBox;
    @FXML
    private Button deleteButton;

    @FXML
    private void initialize() {
        FxUtils.associateButtonWithControls(deleteButton, accountToDeleteComboBox, substituteAccountComboBox);
        FxUtils.setComboBoxItems(accountToDeleteComboBox, Core.getInstance().financeService().getAllAccounts());
        FxUtils.setComboBoxItems(substituteAccountComboBox, Core.getInstance().financeService().getAllAccounts());

        deleteButton.setOnAction(event -> {
            try {
                Core.getInstance().xxxService().delete(accountToDeleteComboBox.getValue(), substituteAccountComboBox.getValue());
                new StandardDialogBuilder().openInfoDialog("Success");
            } catch (Exception e) {
                new StandardDialogBuilder().setSize(1000, 1000).openInfoDialog(e.getMessage());
            }
        });
    }
}
