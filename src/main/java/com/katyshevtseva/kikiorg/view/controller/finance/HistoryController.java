package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.Operation;
import com.katyshevtseva.kikiorg.view.controller.dialog.QuestionDialogController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


class HistoryController implements FxController {
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

    @FXML
    private void initialize() {
        adjustColumns();
        fillTable();
        setRowsColors();
    }

    private void adjustColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        adjustButtonColumn();
    }

    private void fillTable() {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        operations.addAll(Core.getInstance().financeOperationService().getOperationsAvailableForCurrentUser());
        table.setItems(operations);
    }

    private void adjustButtonColumn() {
        deleteColumn.setCellFactory(new Callback<TableColumn<Operation, Void>, TableCell<Operation, Void>>() {
            @Override
            public TableCell<Operation, Void> call(final TableColumn<Operation, Void> param) {
                return new TableCell<Operation, Void>() {

                    private final Button button = new Button();

                    {
                        Utils.setImageOnButton("delete.png", button, 20);
                        button.setOnAction((ActionEvent event) ->
                                OrganizerWindowCreator.getInstance().openQuestionDialog(new QuestionDialogController(
                                        "Delete?",
                                        b -> {
                                            if (b) {
                                                Core.getInstance().financeOperationService().deleteOperation(
                                                        getTableView().getItems().get(getIndex()));
                                                fillTable();
                                            }
                                        })));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        });
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
                            if (operation.getType() == Operation.OperationType.EXPENSE) {
                                setStyle(Utils.getOrangeBackground());
                            } else if (operation.getType() == Operation.OperationType.REPLENISHMENT) {
                                setStyle(Utils.getGreenBackground());
                            } else if (operation.getType() == Operation.OperationType.TRANSFER) {
                                setStyle(Utils.getBlueBackground());
                            }
                        }
                    }
                };
            }
        });
    }
}
