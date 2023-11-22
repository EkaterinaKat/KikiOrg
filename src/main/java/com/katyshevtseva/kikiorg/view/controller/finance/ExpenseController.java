package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.Necessity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
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
    private final Expense expense;
    private final NoArgsKnob operationListener;
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
    @FXML
    private TextField commentTextField;
    @FXML
    private ComboBox<Necessity> necessityComboBox;

    public ExpenseController(Expense expense, NoArgsKnob operationListener) {
        this.expense = expense;
        this.operationListener = operationListener;
    }

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveExpense());
        associateButtonWithControls(doneButton, amountTextField, accountComboBox, itemComboBox, datePicker, necessityComboBox);
        adjustComboBoxes();
        datePicker.setValue(LocalDate.now());

        if (expense != null) {
            accountComboBox.setValue(expense.getAccount());
            itemComboBox.setValue(expense.getItem());
            amountTextField.setText(expense.getAmount().toString());
            FxUtils.setDate(datePicker, expense.getDate());
            commentTextField.setText(expense.getComment());
            necessityComboBox.setValue(expense.getNecessity());
        }
    }

    public void adjustComboBoxes() {
        setItemComboBoxItems();
        setComboBoxItemsAndSetSelectedFirstItem(accountComboBox, Core.getInstance().financeService().getActiveAccounts());
        setComboBoxItems(necessityComboBox, Necessity.values());
    }

    private void setItemComboBoxItems() {
        ItemSelectDialogController itemSelectController = new ItemSelectDialogController(item -> itemComboBox.setValue(item));
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
                        .openNoFxmlContainerDialog(itemSelectController);
            }
        });
    }

    private void saveExpense() {
        if (expense == null) {
            Core.getInstance().financeService().addExpense(
                    accountComboBox.getValue(),
                    Long.parseLong(amountTextField.getText()),
                    itemComboBox.getValue(),
                    java.sql.Date.valueOf(datePicker.getValue()),
                    necessityComboBox.getValue(),
                    commentTextField.getText());
            amountTextField.clear();
            necessityComboBox.setValue(null);
            commentTextField.clear();
        } else {
            Core.getInstance().financeService().editExpense(
                    expense,
                    accountComboBox.getValue(),
                    Long.parseLong(amountTextField.getText()),
                    itemComboBox.getValue(),
                    java.sql.Date.valueOf(datePicker.getValue()),
                    necessityComboBox.getValue(),
                    commentTextField.getText());
            FxUtils.closeWindowThatContains(accountComboBox);
        }
        operationListener.execute();
    }
}
