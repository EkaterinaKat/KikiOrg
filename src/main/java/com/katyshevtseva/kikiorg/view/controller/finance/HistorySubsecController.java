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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


class HistorySubsecController implements FxController {
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
    }

    private void adjustColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        deleteColumn.setCellFactory(param -> new TableCell<Operation, Void>() {
            private final Button button = new Button();

            {
                Utils.setImageOnButton("delete.png", button, 20);

                button.setOnAction((ActionEvent event) ->
                        OrganizerWindowCreator.getInstance().openQuestionDialog(new QuestionDialogController(
                                "Delete?",
                                b -> {
                                    if (b) {
                                        Core.getInstance().financeOperationService().deleteOperation(getTableView().getItems().get(getIndex()));
                                        fillTable();
                                    }
                                })));
            }
        });
    }

    private void fillTable() {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        operations.addAll(Core.getInstance().financeOperationService().getOperationsAvailableForCurrentUser());
        table.setItems(operations);
    }
}
