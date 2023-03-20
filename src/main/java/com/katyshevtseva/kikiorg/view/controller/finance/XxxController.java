package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.AccountDeleteService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemMergeService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

public class XxxController implements SectionController {
    private final AccountDeleteService accountDeleteService = Core.getInstance().accountDeleteService();
    private final ItemMergeService itemMergeService = Core.getInstance().itemMergeService();
    @FXML
    private ComboBox<Account> accountToDeleteComboBox;
    @FXML
    private ComboBox<Account> substituteAccountComboBox;
    @FXML
    private Button deleteButton;
    @FXML
    private ComboBox<Item> itemToMergeComboBox;
    @FXML
    private ComboBox<Item> destItemComboBox;
    @FXML
    private Button mergeButton;

    @FXML
    private void initialize() {
        adjustAccountDeletion();
        adjustItemMerge();
    }

    private void adjustItemMerge() {
        FxUtils.associateButtonWithControls(mergeButton, itemToMergeComboBox, destItemComboBox);
        setComboBoxItems(itemToMergeComboBox, Core.getInstance().financeService().getAllItems());
        setComboBoxItems(destItemComboBox, Core.getInstance().financeService().getAllItems());

        mergeButton.setOnAction(event -> {
            try {
                itemMergeService.merge(itemToMergeComboBox.getValue(), destItemComboBox.getValue());
                new StandardDialogBuilder().openInfoDialog("Success");
                setComboBoxItems(itemToMergeComboBox, Core.getInstance().financeService().getAllItems());
                setComboBoxItems(destItemComboBox, Core.getInstance().financeService().getAllItems());
            } catch (Exception e) {
                new StandardDialogBuilder().setSize(1000, 1000).openInfoDialog(e.getMessage());
            }
        });
    }

    private void adjustAccountDeletion() {
        FxUtils.associateButtonWithControls(deleteButton, accountToDeleteComboBox, substituteAccountComboBox);
        setComboBoxItems(accountToDeleteComboBox, Core.getInstance().financeService().getAllAccounts());

        accountToDeleteComboBox.setOnAction(event -> setComboBoxItems(substituteAccountComboBox, accountDeleteService.getSubstituteAccounts(accountToDeleteComboBox.getValue())));

        deleteButton.setOnAction(event -> {
            try {
                accountDeleteService.delete(accountToDeleteComboBox.getValue(), substituteAccountComboBox.getValue());
                new StandardDialogBuilder().openInfoDialog("Success");
                setComboBoxItems(accountToDeleteComboBox, Core.getInstance().financeService().getAllAccounts());
                setComboBoxItems(substituteAccountComboBox, new ArrayList<>());
            } catch (Exception e) {
                new StandardDialogBuilder().setSize(1000, 1000).openInfoDialog(e.getMessage());
            }
        });
    }
}
