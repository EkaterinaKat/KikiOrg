package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import static com.katyshevtseva.fx.FxUtils.*;

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
    private TableColumn<Item, Void> editColumn;

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        addItemButton.setOnAction(event -> {
            addItem();
            fillTable();
        });
        doneButton.setOnAction(event -> saveExpense());
        associateButtonWithControls(addItemButton, itemTitleField, itemDescArea);
        associateButtonWithControls(doneButton, amountTextField, accountComboBox, itemComboBox, datePicker);
        setItemComboBoxItems();
        setComboBoxItemsAndSetSelectedFirstItem(accountComboBox, Core.getInstance().financeService().getAllAccounts());
        datePicker.setValue(new java.sql.Date(DateCorrector.getProperDate().getTime()).toLocalDate());
        adjustColumns();
        fillTable();
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descColumn.setCellFactory(tc -> {
            TableCell<Item, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        FxUtils.adjustButtonColumn(editColumn, "Edit",
                item -> OrgUtils.getDialogBuilder().openTextFieldAndTextAreaDialog(item.getTitle(), item.getDescription(),
                        (title, desc) -> {
                            Core.getInstance().financeService().alterItem(item, title, desc);
                            fillTable();
                        }));
    }

    private void fillTable() {
        ObservableList<Item> items = FXCollections.observableArrayList();
        items.addAll(Core.getInstance().financeService().getAllItems());
        table.setItems(items);
    }

    private void setItemComboBoxItems() {
        ObservableList<Item> items = FXCollections.observableArrayList(Core.getInstance().financeService().getFewLastItems());

        Item more = new Item();
        more.setTitle("More..");
        items.add(more);
        itemComboBox.setItems(items);

        itemComboBox.setOnAction(event -> {
            if (itemComboBox.getValue() == more) {
                OrganizerWindowCreator.getInstance().openItemSelectDialog(new ItemSelectDialogController(
                        item -> itemComboBox.setValue(item)));
            }
        });
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
