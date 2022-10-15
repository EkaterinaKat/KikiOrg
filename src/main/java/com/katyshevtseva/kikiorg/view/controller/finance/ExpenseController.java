package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.ITEM_SELECT_DIALOG_SIZE;

class ExpenseController implements FxController {
    private final NoArgsKnob operationListener;
    private final ItemSelectDialogController itemSelectDialogController;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private ComboBox<Item> itemComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button doneButton;

    ExpenseController(NoArgsKnob operationListener) {
        this.operationListener = operationListener;
        itemSelectDialogController = new ItemSelectDialogController(item -> itemComboBox.setValue(item));
    }

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveExpense());
        associateButtonWithControls(doneButton, amountTextField, accountComboBox, itemComboBox, datePicker);
        setItemComboBoxItems();
        setComboBoxItemsAndSetSelectedFirstItem(accountComboBox, Core.getInstance().financeService().getActiveAccounts());
        datePicker.setValue(LocalDate.now());
    }

    private void setItemComboBoxItems() {
        ObservableList<Item> items = FXCollections.observableArrayList(Core.getInstance().financeService().getFewLastItems());

        Item more = new Item();
        more.setTitle("More..");
        items.add(more);
        itemComboBox.setItems(items);

        itemComboBox.setOnAction(event -> {
            if (itemComboBox.getValue() == more) {
                new StandardDialogBuilder()
                        .setSize(ITEM_SELECT_DIALOG_SIZE)
                        .setTitle("Select item")
                        .openNoFxmlContainerDialog(itemSelectDialogController);
            }
        });
    }

    private void saveExpense() {
        Core.getInstance().financeService().addExpense(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                itemComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
        operationListener.execute();
    }
}
