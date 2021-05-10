package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.Operation;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService.OperationType;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import static com.katyshevtseva.fx.FxUtils.setImageOnButton;
import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;


class HistoryController implements FxController {
    @FXML
    private Pane searchPane;
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
        searchPane.getChildren().add(OrganizerWindowCreator.getInstance().getSearchNode(new SearchController()));
        adjustColumns();
        fillTable();
        setRowsColors();
    }

    private void adjustColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromTitle"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toTitle"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountString"));
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
                        setImageOnButton("images/delete.png", button, 20);
                        button.setOnAction(event -> {
                            OrgUtils.getDialogBuilder().openQuestionDialog("Delete?", b -> {
                                if (b) {
                                    Core.getInstance().financeOperationService().deleteOperation(
                                            getTableView().getItems().get(getIndex()));
                                    fillTable();
                                }
                            });
                        });
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
                            if (operation.getType() == OperationType.EXPENSE) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, ORANGE));
                            } else if (operation.getType() == OperationType.REPLENISHMENT) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, SCREAMING_GREEN));
                            } else if (operation.getType() == OperationType.TRANSFER) {
                                setStyle(Styler.getColorfullStyle(BACKGROUND, BLUE));
                            }
                        }
                    }
                };
            }
        });
    }
}
