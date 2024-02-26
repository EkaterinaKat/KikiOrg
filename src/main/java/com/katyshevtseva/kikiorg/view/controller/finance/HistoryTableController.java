package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.general.OneOutKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Expense;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Replenishment;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Transfer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;

import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.*;

class HistoryTableController implements FxController {
    private final OneOutKnob<List<Operation>> operationSupplier;
    private NoArgsKnob operationUpdateListener;
    @FXML
    private TableView<Operation> table;
    @FXML
    private TableColumn<Operation, String> dateColumn;
    @FXML
    private TableColumn<Operation, String> fromColumn;
    @FXML
    private TableColumn<Operation, String> toColumn;
    @FXML
    private TableColumn<Operation, String> amountColumn;
    @FXML
    private TableColumn<Operation, String> additionalInfoColumn;
    @FXML
    private TableColumn<Operation, String> commentColumn;

    HistoryTableController(OneOutKnob<List<Operation>> operationSupplier) {
        this.operationSupplier = operationSupplier;
    }

    HistoryTableController(OneOutKnob<List<Operation>> operationSupplier, NoArgsKnob operationUpdateListener) {
        this.operationSupplier = operationSupplier;
        this.operationUpdateListener = operationUpdateListener;
    }

    @FXML
    private void initialize() {
        adjustColumns();
        tuneRows();
        updateTableContent();
    }

    private void adjustColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromTitle"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toTitle"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountString"));
        additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }

    void updateTableContent() {
        setTableContent(operationSupplier.execute());
    }

    void setTableContent(List<Operation> operations) {
        table.getItems().clear();
        ObservableList<Operation> operationObservableList = FXCollections.observableArrayList();
        operationObservableList.addAll(operations);
        table.setItems(operationObservableList);
    }

    private void tuneRows() {
        table.setRowFactory(new Callback<TableView<Operation>, TableRow<Operation>>() {
            @Override
            public TableRow<Operation> call(TableView<Operation> tableView) {

                return new TableRow<Operation>() {
                    @Override
                    protected void updateItem(Operation operation, boolean empty) {
                        super.updateItem(operation, empty);
                        if (operation != null) {
                            if (operation.getType() == FinanceOperationService.OperationType.EXPENSE) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, ORANGE));
                            } else if (operation.getType() == FinanceOperationService.OperationType.REPLENISHMENT) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, SCREAMING_GREEN));
                            } else if (operation.getType() == FinanceOperationService.OperationType.TRANSFER) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, BLUE));
                            }
                            setTooltip(new Tooltip(operation.getAdditionalInfo()));
                            setContextMenu(getOperationContextMenu(operation));
                        }
                    }
                };
            }
        });
    }

    private ContextMenu getOperationContextMenu(Operation operation) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        setEditMenuItemListener(editItem, operation);
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().operationDeletionService().deleteOperation(operation);
                updateTableContent();
                if (operationUpdateListener != null)
                    operationUpdateListener.execute();
            }
        }));
        contextMenu.getItems().add(deleteItem);

        return contextMenu;
    }

    private void setEditMenuItemListener(MenuItem item, Operation operation) {
        if (operation instanceof Expense) {
            item.setOnAction(event1 -> WindowBuilder.openDialog(EXPENSE_EDIT,
                    new ExpenseController((Expense) operation, () -> {
                        updateTableContent();
                        if (operationUpdateListener != null)
                            operationUpdateListener.execute();
                    })));
        }
        if (operation instanceof Replenishment) {
            item.setOnAction(event1 -> WindowBuilder.openDialog(REPLENISHMENT_EDIT,
                    new ReplenishmentController((Replenishment) operation, () -> {
                        updateTableContent();
                        if (operationUpdateListener != null)
                            operationUpdateListener.execute();
                    })));
        }
        if (operation instanceof Transfer) {
            item.setOnAction(event1 -> WindowBuilder.openDialog(TRANSFER_EDIT,
                    new TransferController((Transfer) operation, () -> {
                        updateTableContent();
                        if (operationUpdateListener != null)
                            operationUpdateListener.execute();
                    })));
        }
    }
}
