package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.TableUtils;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.Currency;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd;
import com.katyshevtseva.kikiorg.core.sections.finance.OperationEnd.OperationEndType;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.Arrays;

import static com.katyshevtseva.kikiorg.core.sections.finance.Currency.RUB;

class AdminController implements SectionController {
    private final FinanceService financeService = Core.getInstance().financeService;
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
    private TableColumn<OperationEnd, Void> archiveColumn;

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItemsAndSetSelectedFirstItem(typeComboBox, Arrays.asList(OperationEndType.values()));
        typeComboBox.setOnAction(event -> updateTable());
        updateTable();
    }

    private void updateTable() {
        ObservableList<OperationEnd> items = FXCollections.observableArrayList();
        switch (typeComboBox.getValue()) {
            case ITEM:
                items.addAll(financeService.getAllItems());
                archiveColumn.setVisible(false);
                break;
            case SOURCE:
                items.addAll(financeService.getAllSources());
                archiveColumn.setVisible(false);
                break;
            case ACCOUNT:
                items.addAll(financeService.getAllAccounts());
                archiveColumn.setVisible(true);
        }
        adjustColumns();
        table.setItems(items);
        newButton.setOnAction(event -> newButtonListener());
    }

    private void newButtonListener() {
        DcTextField titleField = new DcTextField(true, "");
        DcTextArea descField = new DcTextArea(false, "");
        DcComboBox<Currency> currencyCB = new DcComboBox<>(true, RUB, Arrays.asList(Currency.values()));

        if (typeComboBox.getValue() == OperationEndType.ACCOUNT) {
            DialogConstructor.constructDialog(() -> {
                financeService
                        .addAccount(titleField.getValue(), descField.getValue(), currencyCB.getValue());
                updateTable();
            }, titleField, currencyCB, descField);
        } else {
            DialogConstructor.constructDialog(() -> {
                switch (typeComboBox.getValue()) {
                    case ITEM:
                        financeService.addItem(titleField.getValue(), descField.getValue());
                        break;
                    case SOURCE:
                        financeService.addSource(titleField.getValue(), descField.getValue());
                }
                updateTable();
            }, titleField, descField);
        }
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titleWithAdditionalInfo"));
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

        TableUtils.adjustButtonColumn(editColumn, "Edit", operationEnd -> {
            DcTextField titleField = new DcTextField(true, operationEnd.getTitle());
            DcTextArea descField = new DcTextArea(false, operationEnd.getDescription());
            DialogConstructor.constructDialog(() -> {
                switch (operationEnd.getType()) {
                    case ITEM:
                        financeService.alterItem((Item) operationEnd, titleField.getValue(), descField.getValue());
                        break;
                    case SOURCE:
                        financeService.alterSource((Source) operationEnd, titleField.getValue(), descField.getValue());
                        break;
                    case ACCOUNT:
                        financeService.alterAccount((Account) operationEnd, titleField.getValue(), descField.getValue());
                }
                updateTable();
            }, titleField, descField);
        });

        TableUtils.adjustButtonColumn(archiveColumn, "-", operationEnd -> {
            if (operationEnd.getType() == OperationEndType.ACCOUNT) {
                financeService.archive((Account) operationEnd);
                updateTable();
            }
        }, button -> button.setText("Archive"));
    }
}
