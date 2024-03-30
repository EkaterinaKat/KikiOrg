package com.katyshevtseva.kikiorg.view.controller.finance.planning;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.Necessity;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.view.controller.finance.ItemSelectDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.ITEM_SELECT_DIALOG_SIZE;

class PotentialExpenseController implements WindowBuilder.FxController {
    private final NoArgsKnob operationListener;
    @FXML
    private ComboBox<Item> itemComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private Button doneButton;
    @FXML
    private TextField commentTextField;
    @FXML
    private ComboBox<Necessity> necessityComboBox;

    public PotentialExpenseController(NoArgsKnob operationListener) {
        this.operationListener = operationListener;
    }

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveExpense());
        associateButtonWithControls(doneButton, amountTextField, itemComboBox, necessityComboBox);
        adjustComboBoxes();
    }

    public void adjustComboBoxes() {
        setItemComboBoxItems();
        setComboBoxItems(necessityComboBox, Necessity.values());
    }

    private void setItemComboBoxItems() {
        ItemSelectDialogController itemSelectController = new ItemSelectDialogController(item -> itemComboBox.setValue(item));
        ObservableList<Item> items = FXCollections.observableArrayList(Core.getInstance().financeService.getFewLastItems());

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
        Core.getInstance().planningService.addPotentialExpense(
                Long.parseLong(amountTextField.getText()),
                itemComboBox.getValue(),
                necessityComboBox.getValue(),
                commentTextField.getText());
        amountTextField.clear();
        operationListener.execute();
        necessityComboBox.setValue(null);
        commentTextField.clear();
    }
}

