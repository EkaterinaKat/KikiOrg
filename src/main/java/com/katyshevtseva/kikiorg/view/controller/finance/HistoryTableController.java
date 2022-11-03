package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.TableUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.setImageOnButton;
import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;

class HistoryTableController implements FxController {
    private List<Operation> operations;
    private NoArgsKnob operationDeleteListener;
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
    private TableColumn<Operation, Void> deleteColumn;

    HistoryTableController(List<Operation> initOperationList) {
        operations = initOperationList;
    }

    HistoryTableController(List<Operation> operations, NoArgsKnob operationDeleteListener) {
        this.operations = operations;
        this.operationDeleteListener = operationDeleteListener;
    }

    @FXML
    private void initialize() {
        adjustColumns();
        setRowsColors();
        setTableContent(operations);
    }

    private void adjustColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromTitle"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toTitle"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountString"));
        TableUtils.adjustButtonColumn(deleteColumn, "",
                operation ->
                        new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
                            if (b) {
                                Core.getInstance().financeOperationService().deleteOperation(operation);
                                operations.remove(operation);
                                setTableContent(operations);
                                if (operationDeleteListener != null)
                                    operationDeleteListener.execute();
                            }
                        }),
                button -> setImageOnButton("images/delete.png", button, 20));
    }

    void setTableContent(List<Operation> operations) {
        this.operations = operations;

        table.getItems().clear();
        ObservableList<Operation> operationObservableList = FXCollections.observableArrayList();
        operationObservableList.addAll(operations);
        table.setItems(operationObservableList);
    }

    private void setRowsColors() {
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
                        }
                    }
                };
            }
        });
    }
}
