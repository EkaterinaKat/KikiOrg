package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.XxxService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

public class XxxController implements FxController {
    private final XxxService service = Core.getInstance().xxxService();
    @FXML
    private ComboBox<Account> accountToDeleteComboBox;
    @FXML
    private ComboBox<Account> substituteAccountComboBox;
    @FXML
    private Button deleteButton;

    @FXML
    private void initialize() {
        FxUtils.associateButtonWithControls(deleteButton, accountToDeleteComboBox, substituteAccountComboBox);
        setComboBoxItems(accountToDeleteComboBox, Core.getInstance().financeService().getAllAccounts());

        accountToDeleteComboBox.setOnAction(event -> setComboBoxItems(substituteAccountComboBox, service.getSubstituteAccounts(accountToDeleteComboBox.getValue())));

        deleteButton.setOnAction(event -> {
            try {
                service.delete(accountToDeleteComboBox.getValue(), substituteAccountComboBox.getValue());
                new StandardDialogBuilder().openInfoDialog("Success");
                setComboBoxItems(accountToDeleteComboBox, Core.getInstance().financeService().getAllAccounts());
                setComboBoxItems(substituteAccountComboBox, new ArrayList<>());
            } catch (Exception e) {
                new StandardDialogBuilder().setSize(1000, 1000).openInfoDialog(e.getMessage());
            }
        });
    }
}
