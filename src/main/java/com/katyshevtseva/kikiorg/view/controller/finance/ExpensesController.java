package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.LocalDate;

class ExpensesController implements FxController {
    @FXML
    private TextField itemTitleField;
    @FXML
    private TextArea itemDescArea;
    @FXML
    private Button addItemButton;
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
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, String> titleColumn;
    @FXML
    private TableColumn<Item, String> descColumn;
    @FXML
    private TableColumn<Item, String> ownerColumn;

    @FXML
    private void initialize() {
        Utils.disableNonNumericChars(amountTextField);
        addItemButton.setOnAction(event -> {
            addItem();
            fillTable();
        });
        doneButton.setOnAction(event -> saveExpense());
        Utils.associateButtonWithControls(addItemButton, itemTitleField, itemDescArea);
        Utils.associateButtonWithControls(doneButton, amountTextField, accountComboBox, itemComboBox, datePicker);
        setItemComboBoxItems();
        setAccountComboBoxItems();
        datePicker.setValue(LocalDate.now());
        adjustColumns();
        fillTable();
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        descColumn.setCellFactory(tc -> {
            TableCell<Item, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private void fillTable() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        items.addAll(Core.getInstance().financeService().getItemsForCurrentOwner());
        table.setItems(items);
    }

    private void setItemComboBoxItems() {
        ObservableList<Item> items = FXCollections.observableArrayList(Core.getInstance().financeService().getItemsForCurrentOwner());
        itemComboBox.setItems(items);
    }

    private void setAccountComboBoxItems() {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(Core.getInstance().financeService().getAccountsForCurrentUser());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }

    private void addItem() {
        Core.getInstance().financeService().addItem(itemTitleField.getText(), itemDescArea.getText());
        itemTitleField.clear();
        itemDescArea.setText("-");
        setItemComboBoxItems();
    }

    private void saveExpense() {
        Core.getInstance().financeService().addExpense(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                itemComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
    }
}
