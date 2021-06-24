package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd.OperationEndType;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.Arrays;

class AdminController implements FxController {
    @FXML
    private ComboBox<OperationEndType> typeComboBox;
    @FXML
    private Button newButton;
    @FXML
    private TableView<OperationEnd> table;
    @FXML
    private TableColumn<OperationEnd, String> titleColumn;
    @FXML
    private TableColumn<OperationEnd, String> descColumn;
    @FXML
    private TableColumn<OperationEnd, Void> editColumn;

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItemsAndSetSelectedFirstItem(typeComboBox, Arrays.asList(OperationEndType.values()));
        typeComboBox.setOnAction(event -> updateTable());
        newButton.setOnAction(event -> newButtonListener());
        adjustColumns();
        updateTable();
    }

    private void updateTable() {
        ObservableList<OperationEnd> items = FXCollections.observableArrayList();
        switch (typeComboBox.getValue()) {
            case ITEM:
                items.addAll(Core.getInstance().financeService().getAllItems());
                break;
            case SOURCE:
                items.addAll(Core.getInstance().financeService().getAllSources());
                break;
            case ACCOUNT:
                items.addAll(Core.getInstance().financeService().getAllAccounts());
        }
        table.setItems(items);
    }

    private void newButtonListener() {
        OrgUtils.getDialogBuilder().openTextFieldAndTextAreaDialog("", "", (title, desc) -> {
            switch (typeComboBox.getValue()) {
                case ITEM:
                    Core.getInstance().financeService().addItem(title, desc);
                    break;
                case SOURCE:
                    Core.getInstance().financeService().addSource(title, desc);
                    break;
                case ACCOUNT:
                    Core.getInstance().financeService().addAccount(title, desc);
            }
            updateTable();
        });
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descColumn.setCellFactory(tc -> {
            TableCell<OperationEnd, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        FxUtils.adjustButtonColumn(editColumn, "Edit", operationEnd ->
                OrgUtils.getDialogBuilder().openTextFieldAndTextAreaDialog(operationEnd.getTitle(), operationEnd.getDescription(),
                        (title, desc) -> {
                            switch (operationEnd.getType()) {
                                case ITEM:
                                    Core.getInstance().financeService().alterItem((Item) operationEnd, title, desc);
                                    break;
                                case SOURCE:
                                    Core.getInstance().financeService().alterSource((Source) operationEnd, title, desc);
                                    break;
                                case ACCOUNT:
                                    Core.getInstance().financeService().alterAccount((Account) operationEnd, title, desc);
                            }
                            updateTable();
                        }));
    }
}
